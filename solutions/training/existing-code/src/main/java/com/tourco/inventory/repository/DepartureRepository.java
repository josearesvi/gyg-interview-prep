package com.tourco.inventory.repository;

import com.tourco.inventory.model.Departure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface DepartureRepository extends JpaRepository<Departure, Long> {

    List<Departure> findByTourIdOrderByStartsAt(Long tourId);

    /** REQUEST 1 + 2: filter by the local day's [start, end) instant range and by seats, in SQL. */
    @Query("""
            select d from Departure d
            where d.tour.id = :tourId and d.startsAt >= :from and d.startsAt < :to
              and (d.capacity - d.reserved) >= :minSeats
            order by d.startsAt""")
    List<Departure> findAvailable(Long tourId, Instant from, Instant to, int minSeats);

    /**
     * REQUEST 3: atomic, race-free reservation. The check and the increment happen in ONE UPDATE statement, so two
     * concurrent requests can't both see "enough seats". Returns 1 if reserved, 0 if not enough room (or no such departure).
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Departure d set d.reserved = d.reserved + :seats where d.id = :id and d.reserved + :seats <= d.capacity")
    int tryReserve(Long id, int seats);

    /** REQUEST 3: one grouped query instead of one query per tour (N+1). */
    @Query("select d.tour.id, count(d) from Departure d where d.reserved < d.capacity and d.tour.id in :tourIds group by d.tour.id")
    List<Object[]> countOpenDepartures(List<Long> tourIds);
}
