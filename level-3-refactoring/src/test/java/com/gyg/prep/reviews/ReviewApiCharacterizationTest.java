package com.gyg.prep.reviews;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CHARACTERIZATION TESTS: they pin down what the legacy API does TODAY, so you can refactor with a safety net.
 * Rule: all enabled tests must stay green after every refactoring step.
 *
 * The @Disabled tests in {@link KnownBugs} describe behaviour the legacy code gets WRONG. Enable them one at a
 * time, once your refactoring makes each fix easy.
 *
 * Seed data (src/main/resources/data.sql): activity 1 has 3 visible reviews (5, 4, 3) plus 1 flagged one;
 * activity 2 has one 5-star review; activity 3 has none.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewApiCharacterizationTest {

    @Autowired MockMvc mvc;

    ResultActions postReview(String json) throws Exception {
        return mvc.perform(post("/reviews").contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    void createsAReview() throws Exception {
        postReview("""
                {"activityId": 2, "author": "Eve", "rating": 4, "comment": "Lovely ride"}""")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.flagged").value(false));
    }

    @Test
    void flagsReviewsWithBlockedWords() throws Exception {
        postReview("""
                {"activityId": 2, "author": "Eve", "rating": 1, "comment": "This is FRAUD"}""")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flagged").value(true));
    }

    @Test
    void validatesInput() throws Exception {
        postReview("""
                {"activityId": 2, "author": "Eve", "rating": 6}""")
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(notNullValue()));
        postReview("""
                {"activityId": 2, "author": "   ", "rating": 5}""")
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(notNullValue()));
        postReview("""
                {"activityId": 2, "author": "Eve"}""")
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(notNullValue()));
        postReview("""
                {"author": "Eve", "rating": 5}""")
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(notNullValue()));
    }

    @Test
    void unknownActivityIs404() throws Exception {
        postReview("""
                {"activityId": 404, "author": "Eve", "rating": 5}""")
                .andExpect(status().isNotFound());
    }

    @Test
    void listsVisibleReviewsNewestFirst() throws Exception {
        mvc.perform(get("/activities/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].author").value("Ben"))
                .andExpect(jsonPath("$[1].author").value("Chen"))
                .andExpect(jsonPath("$[2].author").value("Ana"))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[0].comment").value("Great but very crowded"))
                .andExpect(jsonPath("$[0].createdAt").value(notNullValue()));
    }

    @Test
    void listsReviewsByRating() throws Exception {
        mvc.perform(get("/activities/1/reviews").param("sort", "rating"))
                .andExpect(jsonPath("$[0].author").value("Ana"))
                .andExpect(jsonPath("$[1].author").value("Ben"))
                .andExpect(jsonPath("$[2].author").value("Chen"));
    }

    @Test
    void ratingSummaryIgnoresFlaggedReviews() throws Exception {
        mvc.perform(get("/activities/1/rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityId").value(1))
                .andExpect(jsonPath("$.average").value(4.0))
                .andExpect(jsonPath("$.count").value(3));
    }

    @Test
    void ratingSummaryWithNoReviews() throws Exception {
        mvc.perform(get("/activities/3/rating"))
                .andExpect(jsonPath("$.average").value(0.0))
                .andExpect(jsonPath("$.count").value(0));
    }

    @Test
    void searchIsCaseInsensitiveAndHidesFlagged() throws Exception {
        mvc.perform(get("/reviews/search").param("q", "GUIDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].author", containsInAnyOrder("Ana", "Chen")));
        mvc.perform(get("/reviews/search").param("q", "scam"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Nested
    class KnownBugs {

        @Test
        @Disabled("Bug: SQL built by string concatenation. Remove @Disabled once fixed.")
        void acceptsApostrophesInComments() throws Exception {
            postReview("""
                    {"activityId": 2, "author": "O'Brien", "rating": 5, "comment": "It's the best"}""")
                    .andExpect(status().isCreated());
        }

        @Test
        @Disabled("Security bug: SQL injection leaks flagged reviews. Remove @Disabled once fixed.")
        void searchIsNotInjectable() throws Exception {
            mvc.perform(get("/reviews/search").param("q", "zzz' OR flagged = true OR comment like '"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @Disabled("Bug: the rating cache is never invalidated. Remove @Disabled once fixed.")
        void ratingIsFreshAfterANewReview() throws Exception {
            mvc.perform(get("/activities/2/rating")).andExpect(jsonPath("$.average").value(5.0));

            postReview("""
                    {"activityId": 2, "author": "Finn", "rating": 3, "comment": "ok"}""")
                    .andExpect(status().isCreated());

            mvc.perform(get("/activities/2/rating"))
                    .andExpect(jsonPath("$.average").value(4.0))
                    .andExpect(jsonPath("$.count").value(2));
        }
    }
}
