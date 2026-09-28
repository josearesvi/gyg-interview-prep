package com.getourguide.interview.repository;

import com.getourguide.interview.entity.Supplier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    // TODO: return real statistics per supplier (number of activities, revenue, average rating)
    @Query(value = "SELECT * FROM getyourguide.supplier", nativeQuery = true)
    List<Object[]> getSupplierStats();
}
