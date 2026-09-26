package com.gyg.prep.experiences.activity;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    /** FEATURE 1: the filter runs in SQL (WHERE UPPER(city) = UPPER(?)) with LIMIT/OFFSET and ORDER BY. */
    Page<Activity> findByCityIgnoreCase(String city, Pageable pageable);

    /**
     * FEATURE 3: SELECT ... FOR UPDATE. Concurrent bookings for the same activity queue up on this row lock until
     * the holder's transaction commits, so "count seats, then insert" becomes atomic per activity.
     * Trade-off: it serialises bookings per activity. That is fine here, and a hot spot for a 10k-ticket concert
     * drop. Alternatives: @Version + retry (optimistic), or an atomic conditional UPDATE on a booked_seats column.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Activity a where a.id = :id")
    Optional<Activity> findByIdForUpdate(long id);
}
