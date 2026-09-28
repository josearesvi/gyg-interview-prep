package com.getourguide.interview.controller;

import com.getourguide.interview.dto.SupplierDto;
import com.getourguide.interview.dto.SupplierStats;
import com.getourguide.interview.service.SupplierService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/** No EntityManager or native SQL in the web layer, and DTOs instead of entities. */
@RestController
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/suppliers")
    public List<SupplierDto> suppliers() {
        return supplierService.getSuppliers();
    }

    @GetMapping("/suppliers/search/{search}")
    public List<SupplierDto> suppliersSearch(@PathVariable String search) {
        return supplierService.search(search);
    }

    @GetMapping("/suppliers/stats")
    public List<SupplierStats> supplierStats() {
        return supplierService.getStats();
    }
}
