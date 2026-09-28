package com.getourguide.interview.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.getourguide.interview.controller.SupplierController;
import com.getourguide.interview.helpers.ActivityHelper;
import com.getourguide.interview.helpers.SupplierHelper;
import com.getourguide.interview.repository.ActivityRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private SupplierController supplierController;

    @InjectMocks
    private ActivityService activityService;

    @Test
    void getActivities() {
        var supplier = SupplierHelper.createSupplier(1L, "Spree Tours GmbH");
        when(activityRepository.findAll()).thenReturn(List.of(ActivityHelper.createActivity(1L, "Boat tour", supplier)));

        var result = activityService.getActivities();

        assertEquals(1, result.size());
        assertEquals("Spree Tours GmbH", result.get(0).getSupplierName());
    }
}
