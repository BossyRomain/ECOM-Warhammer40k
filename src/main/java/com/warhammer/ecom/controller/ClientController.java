package com.warhammer.ecom.controller;

import com.warhammer.ecom.controller.dto.ClientLoginDTO;
import com.warhammer.ecom.controller.dto.ClientSignUpDTO;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/signup")
    public Client singUp(@RequestBody ClientSignUpDTO clientSignUpDTO) {
        return clientService.create(clientSignUpDTO);
    }

    @GetMapping("/login")
    public ResponseEntity<Client> login(@RequestBody ClientLoginDTO clientLoginDTO) {
        try {
            return ResponseEntity.ok().body(clientService.login(clientLoginDTO.getEmail(), clientLoginDTO.getPassword()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        try {
            clientService.delete(clientId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
