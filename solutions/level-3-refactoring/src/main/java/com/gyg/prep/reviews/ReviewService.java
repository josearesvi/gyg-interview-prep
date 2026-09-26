package com.gyg.prep.reviews;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REFACTORED. The business rules live here, not in the controller.
 *
 * The static HashMap cache is gone. It was never invalidated (stale ratings), was not thread-safe, and grew
 * without bound. COUNT/AVG on an indexed column is cheap. If profiling ever shows it is not, add
 * Spring's @Cacheable("ratings") here with @CacheEvict(value = "ratings", key = "#request.activityId") on add().
 */
@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final ContentModeration moderation;
    private final Clock clock;

    public ReviewService(ReviewRepository repository, ContentModeration moderation, Clock clock) {
        this.repository = repository;
        this.moderation = moderation;
        this.clock = clock;
    }

    @Transactional
    public Reviews.Created add(Reviews.CreateRequest request) {
        if (!repository.activityExists(request.activityId())) {
            throw new NotFoundException("activity not found");
        }
        String comment = request.commentOrEmpty();
        boolean flagged = moderation.shouldFlag(comment);
        long id = repository.insert(request.activityId(), request.author().trim(), request.rating(), comment, flagged,
                LocalDateTime.now(clock));
        return new Reviews.Created(id, flagged);
    }

    @Transactional(readOnly = true)
    public List<Reviews.View> list(long activityId, Reviews.SortOrder sort) {
        return repository.findVisible(activityId, sort);
    }

    @Transactional(readOnly = true)
    public Reviews.RatingSummary rating(long activityId) {
        return repository.ratingSummary(activityId);
    }

    @Transactional(readOnly = true)
    public List<Reviews.SearchHit> search(String text) {
        return repository.searchVisible(text);
    }
}
