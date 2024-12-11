package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.repository.AllegianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllegianceService {

    @Autowired
    private AllegianceRepository allegianceRepository;

    public List<Allegiance> getAll() {
        return allegianceRepository.findAll();
    }
}
