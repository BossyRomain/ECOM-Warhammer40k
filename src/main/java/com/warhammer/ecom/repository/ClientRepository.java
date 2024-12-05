package com.warhammer.ecom.repository;

import com.warhammer.ecom.model.Client;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM  Client c WHERE c.id = :clientId")
    Optional<Client> findByIdWithLock(Long clientId);

    @Query("SELECT c FROM Client c WHERE c.user.username = :email")
    Optional<Client> findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.user.id = :userId")
    Optional<Client> findByUserId(Long userId);
}
