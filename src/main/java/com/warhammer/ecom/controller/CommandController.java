package com.warhammer.ecom.controller;

import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.service.CartService;
import com.warhammer.ecom.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/clients/{clientId}/commands")
public class CommandController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ClientService clientService;

    @GetMapping("")
    public List<Cart> getClientCommands(@PathVariable Long clientId) throws AccessDeniedException {
        return cartService.getClientCommands(clientService.getById(clientId));
    }
}
