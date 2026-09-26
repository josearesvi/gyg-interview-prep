package com.example.wishlistapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WishlistController {

    private final WishlistService wishlists;

    public WishlistController(WishlistService wishlists) {
        this.wishlists = wishlists;
    }

    public record ShareResponse(String shareId, String url) {}

    @PostMapping("/travellers/{travellerId}/wishlist")
    @ResponseStatus(HttpStatus.CREATED)
    public WishlistItem add(@PathVariable String travellerId, @Valid @RequestBody AddItemRequest request) {
        return wishlists.add(travellerId, request);
    }

    @GetMapping("/travellers/{travellerId}/wishlist")
    public List<WishlistItem> list(@PathVariable String travellerId, @RequestParam(required = false) String sort) {
        return wishlists.list(travellerId, sort == null ? null : SortOrder.parse(sort));
    }

    @DeleteMapping("/travellers/{travellerId}/wishlist/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable String travellerId, @PathVariable long itemId) {
        wishlists.remove(travellerId, itemId);
    }

    @GetMapping("/travellers/{travellerId}/wishlist/summary")
    public WishlistSummary summary(@PathVariable String travellerId) {
        return wishlists.summary(travellerId);
    }

    @PostMapping("/travellers/{travellerId}/wishlist/share")
    @ResponseStatus(HttpStatus.CREATED)
    public ShareResponse share(@PathVariable String travellerId) {
        String shareId = wishlists.share(travellerId);
        return new ShareResponse(shareId, "/shared/" + shareId);
    }

    @GetMapping("/shared/{shareId}")
    public List<WishlistItem> shared(@PathVariable String shareId) {
        return wishlists.shared(shareId);
    }
}
