package com.tourco.inventory.controller;

import com.tourco.inventory.service.TourService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/tours")
    public List<Map<String, Object>> tours(@RequestParam(required = false) String city) {
        return tourService.listTours(city);
    }

    @GetMapping("/tours/{id}/availability")
    public List<DepartureView> availability(@PathVariable Long id,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return tourService.availability(id, date).stream().map(DepartureView::of).toList();
    }
}
