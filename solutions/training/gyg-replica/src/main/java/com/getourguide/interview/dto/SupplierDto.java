package com.getourguide.interview.dto;

import com.getourguide.interview.entity.Supplier;

public record SupplierDto(Long id, String name, String address, String zip, String city, String country) {

    public static SupplierDto from(Supplier s) {
        return new SupplierDto(s.getId(), s.getName(), s.getAddress(), s.getZip(), s.getCity(), s.getCountry());
    }
}
