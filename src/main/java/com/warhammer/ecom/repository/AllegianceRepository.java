package com.warhammer.ecom.repository;

import com.warhammer.ecom.model.Allegiance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllegianceRepository extends JpaRepository<Allegiance, Long> {
}
