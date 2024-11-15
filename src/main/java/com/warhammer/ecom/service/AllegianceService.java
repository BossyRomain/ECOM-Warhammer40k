package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.repository.AllegianceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
public class AllegianceService {

    @Autowired
    private AllegianceRepository allegianceRepository;

    public Allegiance get(Long allegianceId) throws NoSuchElementException {
        return allegianceRepository.findById(allegianceId).orElseThrow(NoSuchElementException::new);
    }

    public Allegiance create(Allegiance allegiance) {
        return allegianceRepository.save(allegiance);
    }

    public Allegiance update(Allegiance allegiance) throws NoSuchElementException {
        if(allegianceRepository.existsById(allegiance.getId())) {
            return allegianceRepository.save(allegiance);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void delete(Allegiance allegiance) {
        allegianceRepository.delete(allegiance);
    }
}
