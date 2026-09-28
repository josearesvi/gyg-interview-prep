package com.getourguide.interview.repository;

import com.getourguide.interview.dto.SupplierStats;
import com.getourguide.interview.entity.Supplier;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("""
            select s from Supplier s
            where lower(s.name) like :pattern escape '!'
               or lower(s.address) like :pattern escape '!'
               or lower(s.zip) like :pattern escape '!'
               or lower(s.city) like :pattern escape '!'
               or lower(s.country) like :pattern escape '!'
            order by s.id""")
    List<Supplier> search(String pattern);

    @Query("""
            select new com.getourguide.interview.dto.SupplierStats(s.name, count(a), sum(a.price), avg(a.rating))
            from Activity a join a.supplier s
            group by s.id, s.name
            order by count(a) desc, s.name""")
    List<SupplierStats> getSupplierStats();
}
