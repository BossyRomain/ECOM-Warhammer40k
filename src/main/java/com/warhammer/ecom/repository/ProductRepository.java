package com.warhammer.ecom.repository;

import com.warhammer.ecom.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(Long id);

    @Query("""
        SELECT p FROM Product p
        WHERE (:query IS NULL OR lower(p.name) LIKE concat('%', lower(:query), '%')) AND
        p.unitPrice >= :minPrice AND p.unitPrice <= :maxPrice
        AND (:productTypes IS NULL OR p.productType IN :productTypes)
        AND ((:filterGroups = FALSE AND :filterFactions = FALSE) OR
        (:filterGroups = TRUE AND (p.allegiance IS NOT NULL AND (p.allegiance.group IN :groups OR (:filterFactions = TRUE AND p.allegiance.faction IN :factions)))) OR
        (:filterFactions = TRUE AND p.allegiance IS NOT NULL AND p.allegiance.faction IN :factions)
        )""")
    Page<Product> search(Pageable pageable, String query,
                         float minPrice, float maxPrice,
                         List<String> productTypes,
                         boolean filterGroups, List<String> groups,
                         boolean filterFactions, List<String> factions);
}
