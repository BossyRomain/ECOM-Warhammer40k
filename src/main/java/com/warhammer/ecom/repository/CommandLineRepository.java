package com.warhammer.ecom.repository;

import com.warhammer.ecom.domain.CommandLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommandLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandLineRepository extends JpaRepository<CommandLine, Long> {}
