package com.warhammer.ecom.repository;

import com.warhammer.ecom.domain.Allegiance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Allegiance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllegianceRepository extends JpaRepository<Allegiance, Long> {}
