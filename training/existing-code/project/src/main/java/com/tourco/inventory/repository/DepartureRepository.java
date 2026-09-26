package com.tourco.inventory.repository;

import com.tourco.inventory.model.Departure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartureRepository extends JpaRepository<Departure, Long> {

    List<Departure> findByTourIdOrderByStartsAt(Long tourId);
}
