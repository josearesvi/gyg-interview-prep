package com.tourco.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Tour {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String city;
    /** IANA zone of the city where the tour takes place, e.g. "Europe/Rome". */
    private String timeZone;
    private String supplierId;

    protected Tour() {
    }

    public Tour(String name, String city, String timeZone, String supplierId) {
        this.name = name;
        this.city = city;
        this.timeZone = timeZone;
        this.supplierId = supplierId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getTimeZone() { return timeZone; }
    public String getSupplierId() { return supplierId; }
}
