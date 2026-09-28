package com.getourguide.interview.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.getourguide.interview.dto.ActivityDto;
import com.getourguide.interview.error.ActivityNotFoundException;
import com.getourguide.interview.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer slice: real HTTP routing, JSON and error handling, with the service mocked. Fast, and no database.
 * (The original test called the controller method directly, which skips status codes, serialisation and the advice.)
 */
@WebMvcTest(ActivitiesController.class)
class ActivitiesControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ActivityService activityService;

    @Test
    void returnsTheActivity() throws Exception {
        when(activityService.getActivity(1L)).thenReturn(
                ActivityDto.builder().id(1L).title("Boat tour").supplierName("Spree Tours GmbH").build());

        mvc.perform(get("/activities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Boat tour"));
    }

    @Test
    void notFoundBecomes404() throws Exception {
        when(activityService.getActivity(9L)).thenThrow(new ActivityNotFoundException(9L));

        mvc.perform(get("/activities/9")).andExpect(status().isNotFound());
    }
}
