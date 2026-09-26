package com.gyg.prep.reviews;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REFACTORED. The controller only does HTTP: binding, validation (@Valid) and status codes.
 * Errors are mapped once, in {@link ApiExceptionHandler}, instead of a try/catch in every method.
 */
@RestController
public class ReviewController {

    private final ReviewService reviews;

    public ReviewController(ReviewService reviews) {  // constructor injection: final and easy to unit-test
        this.reviews = reviews;
    }

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Reviews.Created addReview(@Valid @RequestBody Reviews.CreateRequest request) {
        return reviews.add(request);
    }

    @GetMapping("/activities/{id}/reviews")
    public List<Reviews.View> reviews(@PathVariable long id,
                                      @RequestParam(defaultValue = "newest") Reviews.SortOrder sort) {
        return reviews.list(id, sort);
    }

    @GetMapping("/activities/{id}/rating")
    public Reviews.RatingSummary rating(@PathVariable long id) {
        return reviews.rating(id);
    }

    @GetMapping("/reviews/search")
    public List<Reviews.SearchHit> search(@RequestParam String q) {
        return reviews.search(q);
    }
}
