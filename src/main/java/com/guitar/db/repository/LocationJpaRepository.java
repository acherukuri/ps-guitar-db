package com.guitar.db.repository;

import com.guitar.db.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationJpaRepository extends JpaRepository<Location, Long> {
    @Query
    List<Location> findByStateStartingWith(String state);

    @Query
    List<Location> findByStateStartingWithOrderByStateDesc(String state);

    @Query
    Location findFirstByStateStartingWithOrderByStateAsc(String state);

    @Query
    List<Location> findByCountryLikeAndStateLike(String country, String state);

    @Query
    List<Location> queryByStateNot(String state);

    @Query
    long countByStateLike(String state);

    long countByStateIgnoreCaseLike(String s);
}
