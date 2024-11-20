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

    @Query("SELECT p FROM Product p WHERE " +
        "p.unitPrice >= :minPrice AND p.unitPrice <= :maxPrice " +
        "AND (:productTypes IS NULL OR p.productType IN :productTypes) " +
        "AND (:groups IS NULL OR p.allegiance.group IN :groups OR (:factions IS NOT NULL AND p.allegiance.faction IN :factions))")
    Page<Product> findFiltered(Pageable pageable, float minPrice, float maxPrice, List<String> productTypes, List<String> groups, List<String> factions);
}
