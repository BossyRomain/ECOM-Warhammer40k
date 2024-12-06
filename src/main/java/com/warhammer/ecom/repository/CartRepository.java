package com.warhammer.ecom.repository;

import com.warhammer.ecom.model.Cart;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cart c WHERE c.id = :cardId")
    Optional<Cart> findByIdWithLock(Long cardId);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.paid = true, c.purchaseDate = :purchaseDate WHERE c.id = :cartId")
    void pay(Long cartId, Timestamp purchaseDate);
}
