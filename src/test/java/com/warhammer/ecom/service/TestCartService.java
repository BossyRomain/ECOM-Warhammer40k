package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Cart;
import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.CartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestCartService {

    private static final Long CLIENT_ID = 1L;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @AfterEach
    void clearClientCurrentCart() {
        Client client = clientService.getById(CLIENT_ID);
        Cart cart = client.getCurrentCart();
        cart.setCommandLines(new ArrayList<>());
        cartRepository.save(cart);
    }

    /**
     * Test l'ajout valide d'un produit dans le panier d'un client.
     */
    @Test
    public void testAddProductValid() throws Exception {
        Client client = clientService.getById(CLIENT_ID);
        Random random = new Random();
        Product product = productService.get(1L);
        int nbProducts = client.getCurrentCart().getCommandLines().size();
        int quantity = random.nextInt(1, 15);

        cartService.addProduct(client, product, quantity);
        assertEquals(nbProducts + 1, client.getCurrentCart().getCommandLines().size());
        assertEquals(product, client.getCurrentCart().getCommandLines().get(nbProducts).getProduct());
        assertEquals(quantity, client.getCurrentCart().getCommandLines().get(nbProducts).getQuantity());
    }

    /**
     * Test l'ajout d'un produit dans le panier d'un client mais avec une quantité nulle ou négative.
     */
    @Test
    public void testAddProductInvalidQuantity() throws Exception {
        Client client = clientService.getById(CLIENT_ID);
        Random random = new Random();
        Product product = productService.get(random.nextLong(1, 10));
        int quantity = 0;

        try {
            cartService.addProduct(client, product, quantity);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Test l'ajout d'un produit dans le panier d'un client mais les quantités demandées dépassent le stock du produit.
     */
    @Test
    public void testAddProductStockOverflow() throws Exception {
        Client client = clientService.getById(CLIENT_ID);
        Random random = new Random();
        Product product = productService.get(random.nextLong(1, 10));
        int quantity = product.getStock() + 1;

        try {
            cartService.addProduct(client, product, quantity);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Test de l'ajout d'un produit deux fois dans le panier d'un client.
     */
    @Test
    public void testAddProductTwice() throws Exception {
        Client client = clientService.getById(CLIENT_ID);
        Product product = productService.get(1L);

        cartService.addProduct(client, product);

        try {
            cartService.addProduct(client, product);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Test le paiement d'un panier par le client propriétaire du panier.
     */
    @Test
    public void testPayValid() throws Exception {
        final int quantity = 3;
        Client client = clientService.getById(CLIENT_ID);
        List<Integer> oldStocks = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            Product product = productService.get(i);
            cartService.addProduct(client, product);
            oldStocks.add(product.getStock());
            cartService.setProductQuantity(client, product, quantity);
        }

        cartService.pay(client);

        for (long i = 1; i <= 10; i++) {
            Product product = productService.get(i);
            System.out.println(product.getName());
            assertEquals(product.getStock(), oldStocks.get((int) i - 1) - quantity);
        }
    }

    /**
     * Test le paiement d'un panier par le client propriétaire du panier mais certains produits sont hors de stock.
     */
    @Test
    public void testPayOutOfStock() throws Exception {
        Client client = clientService.getById(CLIENT_ID);
        List<Integer> oldStocks = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            Product product = productService.get(i);
            cartService.addProduct(client, product);
            oldStocks.add(product.getStock());
            cartService.setProductQuantity(client, product, product.getStock() + 1);
        }

        try {
            cartService.pay(client);
            fail();
        } catch (RuntimeException ignored) {
        }
    }

    /**
     * Test la suppression d'un produit du panier d'un client.
     */
    @Test
    public void testRemoveProduct() throws Exception {
        Client client = clientService.getById(CLIENT_ID);
        Product product = productService.get(1L);

        cartService.addProduct(client, product);

        cartService.removeProduct(client, product);

        Cart cart = client.getCurrentCart();
        assertTrue(cart.getCommandLines().isEmpty());
    }

}
