package com.warhammer.ecom.controller;

import com.warhammer.ecom.model.Allegiance;
import com.warhammer.ecom.service.AllegianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/allegiances")
public class AllegianceController {

    @Autowired
    private AllegianceService allegianceService;

    @GetMapping("")
    public List<Allegiance> getAll() {
        return allegianceService.getAll();
    }
}
