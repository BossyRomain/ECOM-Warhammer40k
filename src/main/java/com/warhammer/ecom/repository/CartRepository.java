package com.warhammer.ecom.repository;

import com.warhammer.ecom.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c from Cart c WHERE c.client.id = :clientId")
    Collection<Cart> getClientCarts(Long clientId);

    @Modifying
    @Query("UPDATE Cart c SET c.paid = true, c.purchaseDate = :purchaseDate WHERE c.id = :cartId")
    void pay(Long cartId, Timestamp purchaseDate);
}
