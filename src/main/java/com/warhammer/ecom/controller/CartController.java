package com.warhammer.ecom.controller;

import com.warhammer.ecom.model.CommandLine;
import com.warhammer.ecom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/api/clients/{clientId}/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Ajoute un produit au panier d'un client
     * @param clientId un identifiant de client
     * @return
     */
    @PostMapping("")
    public ResponseEntity<CommandLine> addProduct(@PathVariable("clientId") Long clientId, @RequestBody Long productId) {
        try {
            CommandLine commandLine = cartService.addProduct(clientId, productId);
            return ResponseEntity.created(new URI("/api/clients/" + clientId + "/carts")).body(commandLine);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        } catch (URISyntaxException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long clientId, @PathVariable Long productId) {
        try {
            cartService.removeProduct(clientId, productId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> setProductQuantity(@PathVariable Long clientId, @PathVariable Long productId, @RequestBody Integer quantity) {
        try {
            cartService.setProductQuantity(clientId, productId, quantity);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pay")
    public ResponseEntity<Void> payCurrentCart(@PathVariable Long clientId) {
        cartService.pay(clientId);
        return ResponseEntity.ok().build();
    }
}
