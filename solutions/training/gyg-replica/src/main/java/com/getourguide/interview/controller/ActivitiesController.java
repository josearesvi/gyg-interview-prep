package com.getourguide.interview.controller;

import com.getourguide.interview.dto.ActivityDto;
import com.getourguide.interview.service.ActivityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/** @RestController (= @Controller + @ResponseBody). The `id == null` check is gone: a path variable is never null. */
@RestController
@RequiredArgsConstructor
public class ActivitiesController {

    private final ActivityService activityService;

    @GetMapping("/activities")
    public List<ActivityDto> activities() {
        return activityService.getActivities();
    }

    @GetMapping("/activities/{id}")
    public ActivityDto activity(@PathVariable Long id) {
        return activityService.getActivity(id);
    }

    @GetMapping("/activities/search/{search}")
    public List<ActivityDto> activitiesSearch(@PathVariable String search) {
        return activityService.searchActivities(search);
    }
}
