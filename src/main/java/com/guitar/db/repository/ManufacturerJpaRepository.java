package com.guitar.db.repository;

import com.guitar.db.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ManufacturerJpaRepository extends JpaRepository<Manufacturer, Long> {
    @Query
    Manufacturer findByNameStartingWith(String name);

    @Query
    List<Manufacturer> findByFoundedDateBefore(Date date);

    @Query
    List<Manufacturer> findByFoundedDateLessThan(Date date);

    @Query
    List<Manufacturer> findByFoundedDateLessThanEqual(Date date);

    @Query
    List<Manufacturer> findByActiveTrue();

    @Query
    List<Manufacturer> findByActiveFalse();

    @Query //  calls the Manufacturer named query
    List<Manufacturer> getAllThatSellAcoustics(String modelType);
}
