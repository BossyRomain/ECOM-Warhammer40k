package com.warhammer.ecom.repository;

import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductType;
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
                 AND p.productType IN :productTypes
                 AND (:filterAllegiances = FALSE OR (p.allegiance IS NOT NULL AND p.allegiance IN :allegiances))
        """)
    Page<Product> search(Pageable pageable, String query,
                         float minPrice, float maxPrice,
                         List<ProductType> productTypes,
                         boolean filterAllegiances,
                         List<Allegiance> allegiances);
}
