package com.gyg.prep.experiences.activity;

import com.gyg.prep.experiences.common.BadRequestException;
import com.gyg.prep.experiences.common.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    static final int DEFAULT_SIZE = 20;
    static final int MAX_SIZE = 100;
    /** Whitelist: never let clients sort by arbitrary (maybe internal or unindexed) columns. */
    static final Set<String> SORTABLE = Set.of("price", "rating", "title");

    private final ActivityRepository activities;

    public ActivityController(ActivityRepository activities) {
        this.activities = activities;
    }

    @GetMapping
    public PageResponse<ActivityResponse> list(@RequestParam(required = false) String city,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "" + DEFAULT_SIZE) int size,
                                               @RequestParam(required = false) String sort) {
        if (page < 0 || size < 1) {
            throw new BadRequestException("page must be >= 0 and size >= 1");
        }
        PageRequest pageable = PageRequest.of(page, Math.min(size, MAX_SIZE), parseSort(sort));
        Page<Activity> result = (city == null || city.isBlank())
                ? activities.findAll(pageable)
                : activities.findByCityIgnoreCase(city.trim(), pageable);
        return PageResponse.of(result, ActivityResponse::of);
    }

    /** "price" | "price,asc" | "rating,desc". Tie-break on id so the pages are stable. */
    static Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by("id");
        }
        String[] parts = sort.split(",");
        String field = parts[0].trim();
        if (!SORTABLE.contains(field) || parts.length > 2) {
            throw new BadRequestException("cannot sort by '" + sort + "'; allowed: " + SORTABLE);
        }
        Sort.Direction direction = parts.length == 2
                ? Sort.Direction.fromOptionalString(parts[1].trim())
                        .orElseThrow(() -> new BadRequestException("direction must be asc or desc"))
                : Sort.Direction.ASC;
        return Sort.by(direction, field).and(Sort.by("id"));
    }

    @GetMapping("/{id}")
    public ActivityResponse get(@PathVariable long id) {
        return activities.findById(id).map(ActivityResponse::of)
                .orElseThrow(() -> new NotFoundException("activity " + id + " not found"));
    }
}
