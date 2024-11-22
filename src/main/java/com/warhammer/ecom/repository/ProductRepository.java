package com.warhammer.ecom.repository;

import com.warhammer.ecom.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE concat('%', lower(:query), '%')")
    Page<Product> searchByName(Pageable pageable, String query);

    @Query("SELECT p FROM Product p WHERE " +
        "p.unitPrice >= :minPrice AND p.unitPrice <= :maxPrice " +
        "AND (:productTypes IS NULL OR p.productType IN :productTypes) " +
        "AND ((:filterGroups = FALSE AND :filterFactions = FALSE) OR " +
        "(:filterGroups = TRUE AND (p.allegiance.group IN :groups OR (:filterFactions = TRUE AND p.allegiance.faction IN :factions))) OR " +
        "(:filterFactions = TRUE AND p.allegiance.faction IN :factions)" +
        ")")
    Page<Product> findFiltered(Pageable pageable, float minPrice, float maxPrice, List<String> productTypes,
                               boolean filterGroups, List<String> groups, boolean filterFactions, List<String> factions);
}
