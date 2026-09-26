package com.tourco.inventory.repository;

import com.tourco.inventory.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {

    List<Tour> findByCityIgnoreCase(String city);
}
