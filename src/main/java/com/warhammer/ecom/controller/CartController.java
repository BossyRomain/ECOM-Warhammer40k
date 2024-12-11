package com.warhammer.ecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.CommandLine;
import com.warhammer.ecom.service.CartService;
import com.warhammer.ecom.service.ClientService;
import com.warhammer.ecom.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/clients/{clientId}/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

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
        @RequestParam(defaultValue = "1") int quantity
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

    @GetMapping("/pay")
    public ResponseEntity<Map<String, Object>> payCurrentCart(
        @PathVariable Long clientId,
        @RequestParam(name = "address", defaultValue = "") String shippingAddress
    ) {
        try {
            Client client = clientService.getById(clientId);
            Cart cart = client.getCurrentCart();
            cartService.pay(cart);
            emailService.sendCartPayValidation(client.getUser().getUsername(), cart, shippingAddress);
        } catch (RuntimeException e) {
            String[] arr = e.getMessage().split(" ");
            String id = arr[arr.length - 1];
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("id", id);
            return ResponseEntity.badRequest().body(response);
        }
        cartService.create(clientService.getById(clientId));
        return ResponseEntity.ok().build();
    }
}
