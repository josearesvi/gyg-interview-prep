package com.getourguide.interview.service;

import com.getourguide.interview.controller.SupplierController;
import com.getourguide.interview.dto.ActivityDto;
import com.getourguide.interview.entity.Activity;
import com.getourguide.interview.error.ActivityNotFoundException;
import com.getourguide.interview.repository.ActivityRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final SupplierController supplierController;

    public List<ActivityDto> getActivities() {
        List<Activity> activities = activityRepository.findAll();
        List<ActivityDto> result = new ArrayList<>();
        activities.stream().forEach(activity -> {
            result.add(ActivityDto.builder()
                    .id(activity.getId())
                    .title(activity.getTitle())
                    .price(activity.getPrice())
                    .currency(activity.getCurrency())
                    .rating(activity.getRating())
                    .specialOffer(activity.isSpecialOffer())
                    .supplierName(Objects.isNull(activity.getSupplier()) ? "" : activity.getSupplier().getName())
                    .build());
        });
        return result;
    }

    public ActivityDto getActivity(Long activityId) throws ActivityNotFoundException {
        if (activityId == null) {
            throw new ActivityNotFoundException("Activity id must not be null");
        }
        List<Activity> activities = activityRepository.findAll();
        List<ActivityDto> result = new ArrayList<>();
        activities.stream().filter(activity -> activityId.equals(activity.getId())).forEach(activity -> {
            result.add(ActivityDto.builder()
                    .id(activity.getId())
                    .title(activity.getTitle())
                    .price(activity.getPrice())
                    .currency(activity.getCurrency())
                    .rating(activity.getRating())
                    .specialOffer(activity.isSpecialOffer())
                    .supplierName(activity.getSupplier().getName())
                    .build());
        });
        if (result.isEmpty()) {
            throw new ActivityNotFoundException("Activity " + activityId + " not found");
        }
        return result.get(0);
    }

    public List<ActivityDto> searchActivities(String search) {
        List<Activity> activities = activityRepository.findAll();
        List<ActivityDto> result = new ArrayList<>();
        List<ActivityDto> activitiesDto = new ArrayList<>();
        activities.stream().filter(a -> a.getTitle().contains(search)).forEach(activity -> {
            activitiesDto.add(ActivityDto.builder()
                    .id(activity.getId())
                    .title(activity.getTitle())
                    .price(activity.getPrice())
                    .currency(activity.getCurrency())
                    .rating(activity.getRating())
                    .specialOffer(activity.isSpecialOffer())
                    .supplierName(activity.getSupplier().getName())
                    .build());
        });
        return result;
    }
}
