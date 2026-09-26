package com.gyg.prep.bookings.activity;

import com.gyg.prep.bookings.common.NotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityRepository activityRepository;

    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    // Also worth saying out loud: findAll() + filtering in memory loads the whole table on every search.
    // Push the filter into the database (a derived query or @Query) and paginate. See level 4.
    @GetMapping
    public List<Activity> search(@RequestParam(required = false) String city,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return activityRepository.findAll().stream()
                .filter(a -> city == null || a.getCity().equalsIgnoreCase(city))           // FIXED bug 5
                .filter(a -> date == null || isAvailableOn(a, date))                        // FIXED bug 6
                .toList();
    }

    /** Inclusive on both ends: available on the first AND the last day of the season. */
    private static boolean isAvailableOn(Activity a, LocalDate date) {
        return !date.isBefore(a.getAvailableFrom()) && !date.isAfter(a.getAvailableTo());
    }

    @GetMapping("/{id}")
    public Activity get(@PathVariable Long id) {
        // FIXED bug 4: orElse(null) serialises as 200 with an empty body
        return activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Activity " + id + " not found"));
    }
}
