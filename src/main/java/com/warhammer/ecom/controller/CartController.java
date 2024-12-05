package com.warhammer.ecom.controller;

import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.CommandLine;
import com.warhammer.ecom.service.CartService;
import com.warhammer.ecom.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/api/clients/{clientId}/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ClientService clientService;

    /**
     * Ajoute un produit au panier d'un client
     *
     * @param clientId un identifiant de client
     * @return la ligne de commande du produit ajouté
     */
    @PostMapping("/{productId}")
    public ResponseEntity<CommandLine> addProduct(
        @PathVariable("clientId") Long clientId,
        @PathVariable("productId") Long productId,
        @RequestParam(value = "quantity", defaultValue = "1") int quantity
    ) throws URISyntaxException {
        CommandLine commandLine = cartService.addProduct(clientId, productId, quantity);
        return ResponseEntity.created(new URI("/api/clients/" + clientId + "/carts")).body(commandLine);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long clientId, @PathVariable Long productId) {
        cartService.removeProduct(clientId, productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> setProductQuantity(@PathVariable Long clientId, @PathVariable Long productId, @RequestBody Integer quantity) {
        cartService.setProductQuantity(clientId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clear(@PathVariable Long clientId) {
        cartService.clear(clientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pay")
    public ResponseEntity<Void> payCurrentCart(@PathVariable Long clientId) {
        Client client = clientService.getById(clientId);
        boolean success = cartService.pay(client.getCurrentCart());
        if (success) {
            cartService.create(client);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
