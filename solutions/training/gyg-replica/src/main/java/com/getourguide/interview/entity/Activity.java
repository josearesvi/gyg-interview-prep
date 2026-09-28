package com.getourguide.interview.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * Ids come from the seed data, so no @GeneratedValue (AUTO would need a sequence table the schema doesn't have).
 *
 * @NotFound(IGNORE) stays for now: the seed data really does contain supplier_id = 5, which doesn't exist. The proper
 * fix is a data-cleanup migration plus a foreign key, so the database can't hold an orphan in the first place.
 * Hibernate also warns that @NotFound forces the association to be loaded eagerly, which is why the repository
 * fetches it with an explicit LEFT JOIN FETCH.
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = "getyourguide", name = "activity")
public class Activity {
    @Id
    private Long id;
    private String title;
    private int price;
    private String currency;
    private double rating;
    private boolean specialOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @ToString.Exclude
    private Supplier supplier;
}
