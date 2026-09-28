package com.getourguide.interview.service;

import com.getourguide.interview.dto.SupplierDto;
import com.getourguide.interview.dto.SupplierStats;
import com.getourguide.interview.repository.LikePattern;
import com.getourguide.interview.repository.SupplierRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<SupplierDto> getSuppliers() {
        return supplierRepository.findAll(Sort.by("id")).stream().map(SupplierDto::from).toList();
    }

    /** Every match (it used to return the first one), and an empty list when nothing matches (it used to return all). */
    public List<SupplierDto> search(String search) {
        return supplierRepository.search(LikePattern.contains(search)).stream().map(SupplierDto::from).toList();
    }

    public List<SupplierStats> getStats() {
        return supplierRepository.getSupplierStats().stream().map(SupplierStats::withRoundedRating).toList();
    }
}
