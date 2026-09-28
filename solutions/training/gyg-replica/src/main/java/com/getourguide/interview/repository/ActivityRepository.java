package com.getourguide.interview.repository;

import com.getourguide.interview.entity.Activity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    /** One query instead of 1 + N supplier look-ups. */
    @Query("select a from Activity a left join fetch a.supplier order by a.id")
    List<Activity> findAllWithSupplier();

    @Query("select a from Activity a left join fetch a.supplier where a.id = :id")
    Optional<Activity> findWithSupplierById(Long id);

    /** Filtering in SQL, not findAll() + Java. `pattern` is already lower-cased, escaped and wrapped in %...%. */
    @Query("select a from Activity a left join fetch a.supplier where lower(a.title) like :pattern escape '!' order by a.id")
    List<Activity> searchByTitle(String pattern);
}
