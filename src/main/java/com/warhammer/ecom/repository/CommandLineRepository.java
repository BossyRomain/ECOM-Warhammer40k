package com.warhammer.ecom.repository;

import com.warhammer.ecom.model.CommandLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandLineRepository extends JpaRepository<CommandLine, Long> {

    @Query("SELECT cl FROM CommandLine cl WHERE cl.product.id = :productId AND cl.command.client.id = :clientId")
    Optional<CommandLine> findByClientAndProduct(Long clientId, Long productId);

    @Query("SELECT cl FROM CommandLine cl WHERE cl.command.id = :commandId")
    List<CommandLine> getAllWithLock(Long commandId);
}
