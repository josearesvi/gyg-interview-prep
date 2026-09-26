package com.gyg.prep.experiences.activity;

import com.gyg.prep.experiences.common.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityRepository activities;

    public ActivityController(ActivityRepository activities) {
        this.activities = activities;
    }

    /** Feature 1 changes this endpoint: filtering, pagination and sorting. See the level README. */
    @GetMapping
    public List<ActivityResponse> list() {
        return activities.findAll().stream().map(ActivityResponse::of).toList();
    }

    @GetMapping("/{id}")
    public ActivityResponse get(@PathVariable long id) {
        return activities.findById(id).map(ActivityResponse::of)
                .orElseThrow(() -> new NotFoundException("activity " + id + " not found"));
    }
}
