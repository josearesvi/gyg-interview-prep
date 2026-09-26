package com.gyg.prep.reviews;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * REFACTORED. All SQL lives here and every value is a bind parameter (:name), which fixes both the SQL injection
 * and the "It's great" apostrophe crash. The generated id comes from the INSERT itself, not from
 * "select max(id)", which races with concurrent inserts.
 */
@Repository
public class ReviewRepository {

    private final JdbcClient jdbc;

    public ReviewRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public boolean activityExists(long activityId) {
        return jdbc.sql("SELECT COUNT(*) FROM activity WHERE id = :id")
                .param("id", activityId)
                .query(Integer.class).single() > 0;
    }

    public long insert(long activityId, String author, int rating, String comment, boolean flagged,
                       LocalDateTime createdAt) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.sql("""
                        INSERT INTO review (activity_id, author, rating, comment, flagged, created_at)
                        VALUES (:activityId, :author, :rating, :comment, :flagged, :createdAt)""")
                .param("activityId", activityId)
                .param("author", author)
                .param("rating", rating)
                .param("comment", comment)
                .param("flagged", flagged)
                .param("createdAt", createdAt)
                .update(keys, "id");
        return Objects.requireNonNull(keys.getKey()).longValue();
    }

    public List<Reviews.View> findVisible(long activityId, Reviews.SortOrder sort) {
        return jdbc.sql("""
                        SELECT id, author, rating, comment, created_at FROM review
                        WHERE flagged = FALSE AND activity_id = :activityId
                        ORDER BY\s""" + sort.orderBy) // enum constant, not user input
                .param("activityId", activityId)
                .query((rs, i) -> new Reviews.View(rs.getLong("id"), rs.getString("author"), rs.getInt("rating"),
                        rs.getString("comment"), rs.getTimestamp("created_at").toLocalDateTime()))
                .list();
    }

    public Reviews.RatingSummary ratingSummary(long activityId) {
        return jdbc.sql("""
                        SELECT COUNT(*) AS n, COALESCE(AVG(CAST(rating AS DOUBLE)), 0) AS avg FROM review
                        WHERE flagged = FALSE AND activity_id = :activityId""")
                .param("activityId", activityId)
                .query((rs, i) -> new Reviews.RatingSummary(activityId,
                        Math.round(rs.getDouble("avg") * 10) / 10.0, rs.getInt("n")))
                .single();
    }

    public List<Reviews.SearchHit> searchVisible(String text) {
        return jdbc.sql("""
                        SELECT id, activity_id, author, rating, comment FROM review
                        WHERE flagged = FALSE AND LOWER(comment) LIKE :pattern ESCAPE '\\'""")
                .param("pattern", "%" + escapeLike(text.toLowerCase()) + "%")
                .query((rs, i) -> new Reviews.SearchHit(rs.getLong("id"), rs.getLong("activity_id"),
                        rs.getString("author"), rs.getInt("rating"), rs.getString("comment")))
                .list();
    }

    /** Without this, searching for "100%" or "_" would act as a wildcard. */
    private static String escapeLike(String s) {
        return s.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
