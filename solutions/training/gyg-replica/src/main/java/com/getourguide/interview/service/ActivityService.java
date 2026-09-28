package com.getourguide.interview.service;

import com.getourguide.interview.dto.ActivityDto;
import com.getourguide.interview.error.ActivityNotFoundException;
import com.getourguide.interview.repository.ActivityRepository;
import com.getourguide.interview.repository.LikePattern;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * No more SupplierController dependency (a service must never depend on the web layer).
 * readOnly = true: Hibernate skips dirty checking and the flush at commit.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<ActivityDto> getActivities() {
        return activityRepository.findAllWithSupplier().stream().map(ActivityDto::from).toList();
    }

    public ActivityDto getActivity(Long activityId) {
        return activityRepository.findWithSupplierById(activityId)
                .map(ActivityDto::from)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));
    }

    public List<ActivityDto> searchActivities(String search) {
        return activityRepository.searchByTitle(LikePattern.contains(search)).stream().map(ActivityDto::from).toList();
    }
}
