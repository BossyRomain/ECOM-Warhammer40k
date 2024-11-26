package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.model.Faction;
import com.warhammer.ecom.model.Group;
import com.warhammer.ecom.repository.AllegianceRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AllegianceService {

    @Autowired
    private AllegianceRepository allegianceRepository;

    private Allegiance emptyAllegiance;

    @PostConstruct
    public void init() {
        Allegiance empty = new Allegiance();
        empty.setGroup(Group.NONE);
        empty.setFaction(Faction.NONE);

        this.emptyAllegiance = allegianceRepository.save(empty);
    }

    public List<Allegiance> getAll() {
        return allegianceRepository.findAll();
    }

    public Allegiance get(Long allegianceId) throws NoSuchElementException {
        return allegianceRepository.findById(allegianceId).orElseThrow(NoSuchElementException::new);
    }

    public Allegiance getEmptyAllegiance() {
        return emptyAllegiance;
    }

    public Allegiance create(Allegiance allegiance) {
        return allegianceRepository.save(allegiance);
    }

    public Allegiance update(Allegiance allegiance) throws NoSuchElementException {
        if (allegianceRepository.existsById(allegiance.getId())) {
            return allegianceRepository.save(allegiance);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void delete(Allegiance allegiance) {
        allegianceRepository.delete(allegiance);
    }
}
