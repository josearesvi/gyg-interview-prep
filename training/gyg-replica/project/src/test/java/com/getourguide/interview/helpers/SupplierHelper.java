package com.getourguide.interview.helpers;

import com.getourguide.interview.entity.Supplier;

public class SupplierHelper {

    public static Supplier createSupplier(Long id, String name) {
        Supplier supplier = new Supplier();
        supplier.setId(id);
        supplier.setName(name);
        supplier.setCity("Berlin");
        supplier.setCountry("Germany");
        return supplier;
    }
}
