package com.gyg.prep.reviews;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** REFACTORED. Typed request/response records replace Map<String, Object>, so the API contract is visible. */
public final class Reviews {

    private Reviews() {
    }

    public record CreateRequest(
            @NotNull(message = "activityId is required") Long activityId,
            @NotBlank(message = "author is required") @Size(max = 100) String author,
            @NotNull(message = "rating is required")
            @Min(value = 1, message = "rating must be between 1 and 5")
            @Max(value = 5, message = "rating must be between 1 and 5") Integer rating,
            @Size(max = 2000, message = "comment too long") String comment) {

        public String commentOrEmpty() {
            return comment == null ? "" : comment;
        }
    }

    public record Created(long id, boolean flagged) {}

    public record View(long id, String author, int rating, String comment, LocalDateTime createdAt) {}

    public record SearchHit(long id, long activityId, String author, int rating, String comment) {}

    public record RatingSummary(long activityId, double average, int count) {}

    public enum SortOrder {
        NEWEST("created_at DESC"),
        RATING("rating DESC, created_at DESC");

        /** A whitelisted ORDER BY clause. Never interpolate user input into SQL, not even a sort key. */
        final String orderBy;

        SortOrder(String orderBy) {
            this.orderBy = orderBy;
        }

        @JsonCreator
        public static SortOrder from(String value) {
            return valueOf(value.trim().toUpperCase());
        }
    }
}
