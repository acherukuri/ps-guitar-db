package com.guitar.db.repository;

import com.guitar.db.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ModelJpaRepository extends JpaRepository<Model, Long> {
    @Query
    List<Model> findByPriceBetween(BigDecimal lowest, BigDecimal highest);

    @Query
    List<Model> findByPriceBetweenAndWoodTypeContaining(BigDecimal lowest, BigDecimal highest, String wood);

    @Query("select m from Model m where m.price >= :lowest and m.price <= :highest and m.woodType like :wood")
    List<Model> queryByPriceRangeAndWoodType(@Param("lowest") BigDecimal lowest,
                                             @Param("highest") BigDecimal highest,
                                             @Param("wood") String wood,
                                             Pageable page); // retuns List object which doesn't give insights into next and previous pages.
    // The above query can written like this too.
//    @Query("select m from Model m where m.price >= ?1 and m.price <= ?2 and m.woodType like ?3")
//    List<Model> queryByPriceRangeAndWoodType(BigDecimal lowest,
//                                             BigDecimal highest,
//                                             String wood);

    List<Model> findByModelTypeNameIn(List<String> types);

    @Query // calls the Model named query
    List<Model> findAllModelsByType(@Param("name") String name);

}
