package com.gyg.prep.bookings.activity;

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

    @GetMapping
    public List<Activity> search(@RequestParam(required = false) String city,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return activityRepository.findAll().stream()
                .filter(a -> city == null || a.getCity().equals(city))
                .filter(a -> date == null || (date.isAfter(a.getAvailableFrom()) && date.isBefore(a.getAvailableTo())))
                .toList();
    }

    @GetMapping("/{id}")
    public Activity get(@PathVariable Long id) {
        return activityRepository.findById(id).orElse(null);
    }
}
