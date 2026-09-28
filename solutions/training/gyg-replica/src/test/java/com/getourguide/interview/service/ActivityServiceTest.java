package com.getourguide.interview.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.getourguide.interview.error.ActivityNotFoundException;
import com.getourguide.interview.helpers.ActivityHelper;
import com.getourguide.interview.helpers.SupplierHelper;
import com.getourguide.interview.repository.ActivityRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Plain unit test. The dead SupplierController mock is gone, since the service no longer depends on it. */
@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    ActivityRepository activityRepository;

    @InjectMocks
    ActivityService activityService;

    @Test
    void missingSupplierMapsToEmptyName() {
        when(activityRepository.findAllWithSupplier()).thenReturn(List.of(
                ActivityHelper.createActivity(1L, "Boat tour", SupplierHelper.createSupplier(1L, "Spree Tours GmbH")),
                ActivityHelper.createActivity(2L, "Bunker tour", null)));

        assertThat(activityService.getActivities())
                .extracting("supplierName").containsExactly("Spree Tours GmbH", "");
    }

    @Test
    void unknownIdThrowsNotFound() {
        when(activityRepository.findWithSupplierById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.getActivity(42L)).isInstanceOf(ActivityNotFoundException.class);
    }
}
