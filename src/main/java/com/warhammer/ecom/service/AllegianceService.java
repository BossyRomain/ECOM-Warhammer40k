package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.repository.AllegianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllegianceService {

    @Autowired
    private AllegianceRepository allegianceRepository;

    public Allegiance create(Allegiance allegiance) {
        return allegianceRepository.save(allegiance);
    }
}
