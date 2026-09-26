package com.example.wishlistapi;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory store (allowed by the requirements). Each traveller's list is a LinkedHashMap (keeps insertion order),
 * and every mutation of one traveller's list happens inside ConcurrentHashMap.compute(), which locks that key, so the
 * duplicate and limit checks are atomic per traveller.
 *
 * Production discussion: this state is per-instance, so with 3 instances you need a database
 * (table wishlist_item with UNIQUE(traveller_id, activity_id) and an index on traveller_id) and a table for share_id.
 */
@Service
public class WishlistService {

    static final int MAX_ITEMS = 20;
    static final String CURRENCY = "EUR";

    private final Map<String, LinkedHashMap<Long, WishlistItem>> itemsByTraveller = new ConcurrentHashMap<>();
    private final Map<String, String> travellerByShareId = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();
    private final SecureRandom random = new SecureRandom();
    private final Clock clock;

    public WishlistService(Clock clock) {
        this.clock = clock;
    }

    public WishlistItem add(String travellerId, AddItemRequest request) {
        WishlistItem[] created = new WishlistItem[1];
        itemsByTraveller.compute(travellerId, (id, items) -> {
            LinkedHashMap<Long, WishlistItem> list = items == null ? new LinkedHashMap<>() : items;
            boolean duplicate = list.values().stream().anyMatch(i -> i.activityId() == request.activityId());
            if (duplicate) {
                throw new ApiException.Duplicate("activity " + request.activityId() + " is already on the wishlist");
            }
            if (list.size() >= MAX_ITEMS) {
                throw new ApiException.LimitReached("a wishlist holds at most " + MAX_ITEMS + " items");
            }
            WishlistItem item = new WishlistItem(ids.incrementAndGet(), request.activityId(), request.title().trim(),
                    request.city().trim(), request.price().setScale(2, RoundingMode.UNNECESSARY), Instant.now(clock));
            list.put(item.id(), item);
            created[0] = item;
            return list;
        });
        return created[0];
    }

    public List<WishlistItem> list(String travellerId, SortOrder sort) {
        List<WishlistItem> items = snapshot(travellerId);
        if (sort != null) {
            items.sort(sort.comparator);
        }
        return items;
    }

    public void remove(String travellerId, long itemId) {
        boolean[] removed = new boolean[1];
        itemsByTraveller.computeIfPresent(travellerId, (id, list) -> {
            removed[0] = list.remove(itemId) != null;
            return list;
        });
        if (!removed[0]) {
            throw new ApiException.NotFound("item " + itemId + " not found");
        }
    }

    public WishlistSummary summary(String travellerId) {
        List<WishlistItem> items = snapshot(travellerId);
        BigDecimal total = items.stream().map(WishlistItem::price).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new WishlistSummary(items.size(), total.setScale(2, RoundingMode.UNNECESSARY), CURRENCY);
    }

    /** 128 random bits, URL-safe: unguessable, unlike a sequence. */
    public String share(String travellerId) {
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String shareId = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        travellerByShareId.put(shareId, travellerId);
        return shareId;
    }

    public List<WishlistItem> shared(String shareId) {
        String travellerId = travellerByShareId.get(shareId);
        if (travellerId == null) {
            throw new ApiException.NotFound("share " + shareId + " not found");
        }
        return snapshot(travellerId);
    }

    private List<WishlistItem> snapshot(String travellerId) {
        List<WishlistItem> copy = new ArrayList<>();
        itemsByTraveller.computeIfPresent(travellerId, (id, list) -> {
            copy.addAll(list.values());
            return list;
        });
        return copy;
    }
}
