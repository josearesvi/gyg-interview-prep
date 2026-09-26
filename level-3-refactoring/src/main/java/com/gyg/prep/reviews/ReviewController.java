package com.gyg.prep.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO clean this up at some point
@RestController
public class ReviewController {

    @Autowired
    JdbcTemplate jdbc;

    // cache so the activity page loads fast
    static Map<Long, Map<String, Object>> cache = new HashMap<>();

    @PostMapping("/reviews")
    public ResponseEntity<Object> addReview(@RequestBody Map<String, Object> body) {
        try {
            System.out.println("adding review " + body);
            if (body.get("activityId") == null) {
                return ResponseEntity.status(400).body(Map.of("error", "activityId is required"));
            } else {
                Long activityId = Long.valueOf(body.get("activityId").toString());
                Integer count = jdbc.queryForObject("select count(*) from activity where id = " + activityId, Integer.class);
                if (count == 0) {
                    return ResponseEntity.status(404).body(Map.of("error", "activity not found"));
                } else {
                    if (body.get("rating") == null) {
                        return ResponseEntity.status(400).body(Map.of("error", "rating is required"));
                    } else {
                        int rating = Integer.parseInt(body.get("rating").toString());
                        if (rating < 1 || rating > 5) {
                            return ResponseEntity.status(400).body(Map.of("error", "rating must be between 1 and 5"));
                        }
                        String author = (String) body.get("author");
                        if (author == null || author.trim().equals("")) {
                            return ResponseEntity.status(400).body(Map.of("error", "author is required"));
                        }
                        String comment = (String) body.getOrDefault("comment", "");
                        if (comment.length() > 2000) {
                            return ResponseEntity.status(400).body(Map.of("error", "comment too long"));
                        }
                        boolean flagged = false;
                        for (String w : new String[]{"scam", "fraud"}) {
                            if (comment.toLowerCase().contains(w)) {
                                flagged = true;
                            }
                        }
                        jdbc.update("insert into review (activity_id, author, rating, comment, flagged, created_at) values ("
                                + activityId + ", '" + author + "', " + rating + ", '" + comment + "', " + flagged
                                + ", CURRENT_TIMESTAMP)");
                        Long id = jdbc.queryForObject("select max(id) from review", Long.class);
                        Map<String, Object> res = new HashMap<>();
                        res.put("id", id);
                        res.put("flagged", flagged);
                        return ResponseEntity.status(201).body(res);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "" + e.getMessage()));
        }
    }

    @GetMapping("/activities/{id}/reviews")
    public ResponseEntity<Object> reviews(@PathVariable Long id, @RequestParam(required = false) String sort) {
        try {
            String order = "created_at desc";
            if (sort != null && sort.equals("rating")) {
                order = "rating desc, created_at desc";
            }
            List<Map<String, Object>> rows = jdbc.queryForList(
                    "select id, author, rating, comment, created_at from review where flagged = false and activity_id = "
                            + id + " order by " + order);
            List<Map<String, Object>> out = new ArrayList<>();
            for (Map<String, Object> r : rows) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", r.get("ID"));
                m.put("author", r.get("AUTHOR"));
                m.put("rating", r.get("RATING"));
                m.put("comment", r.get("COMMENT"));
                m.put("createdAt", r.get("CREATED_AT").toString());
                out.add(m);
            }
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "" + e.getMessage()));
        }
    }

    @GetMapping("/activities/{id}/rating")
    public ResponseEntity<Object> rating(@PathVariable Long id) {
        if (cache.containsKey(id)) {
            return ResponseEntity.ok(cache.get(id));
        }
        List<Integer> ratings = jdbc.queryForList(
                "select rating from review where flagged = false and activity_id = " + id, Integer.class);
        double sum = 0;
        for (int i = 0; i < ratings.size(); i++) {
            sum = sum + ratings.get(i);
        }
        double avg = 0;
        if (ratings.size() > 0) {
            avg = Math.round(sum / ratings.size() * 10) / 10.0;
        }
        Map<String, Object> res = new HashMap<>();
        res.put("activityId", id);
        res.put("average", avg);
        res.put("count", ratings.size());
        cache.put(id, res);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/reviews/search")
    public ResponseEntity<Object> search(@RequestParam String q) {
        try {
            List<Map<String, Object>> rows = jdbc.queryForList(
                    "select id, activity_id, author, rating, comment from review where flagged = false and lower(comment) like '%"
                            + q.toLowerCase() + "%'");
            List<Map<String, Object>> out = new ArrayList<>();
            for (Map<String, Object> r : rows) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", r.get("ID"));
                m.put("activityId", r.get("ACTIVITY_ID"));
                m.put("author", r.get("AUTHOR"));
                m.put("rating", r.get("RATING"));
                m.put("comment", r.get("COMMENT"));
                out.add(m);
            }
            return ResponseEntity.ok(out);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "" + e.getMessage()));
        }
    }
}
