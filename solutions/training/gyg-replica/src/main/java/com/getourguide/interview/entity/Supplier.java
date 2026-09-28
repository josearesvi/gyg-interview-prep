package com.getourguide.interview.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Was @Data: its equals/hashCode/toString walked the lazy `activities` collection (LazyInitializationException, or
 * infinite recursion with Activity), and Jackson serialised the bidirectional graph until the nesting limit.
 * Entities are never returned from controllers now. See SupplierDto.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "getyourguide", name = "supplier")
public class Supplier {
    @Id
    private Long id;
    private String name;
    private String address;
    private String zip;
    private String city;
    private String country;

    @OneToMany(mappedBy = "supplier")
    @ToString.Exclude
    private List<Activity> activities;
}
