package com.tourco.inventory.controller;

import com.tourco.inventory.service.TourService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/tours")
    public List<TourService.TourSummary> tours(@RequestParam(required = false) String city) {
        return tourService.listTours(city);
    }

    /** REQUEST 2: optional minSeats (1..20); Spring's built-in method validation turns violations into 400. */
    @GetMapping("/tours/{id}/availability")
    public List<DepartureView> availability(@PathVariable Long id,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                            @RequestParam(required = false) @Min(1) @Max(20) Integer minSeats) {
        return tourService.availability(id, date, minSeats == null ? 0 : minSeats).stream()
                .map(DepartureView::of).toList();
    }
}
