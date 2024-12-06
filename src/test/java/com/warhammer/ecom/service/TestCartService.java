package com.warhammer.ecom.service;

import com.warhammer.ecom.model.Client;
import com.warhammer.ecom.model.CommandLine;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.repository.ClientRepository;
import com.warhammer.ecom.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestCartService {

    private static final Long CLIENT_ID = 1L;

    @Autowired
    private CartService cartService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Test l'ajout valide d'un produit dans le panier d'un client.
     */
    @Test
    public void testAddProductValid() throws Exception {
        Client client = clientRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        Random random = new Random();
        Product product = productRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        int nbProducts = client.getCurrentCart().getCommandLines().size();
        int quantity = random.nextInt(1, 15);

        cartService.addProduct(client.getId(), 1L, quantity);

        client = clientRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        assertEquals(nbProducts + 1, client.getCurrentCart().getCommandLines().size());

        CommandLine commandLine = client.getCurrentCart().getCommandLines().get(nbProducts);
        assertEquals(product, commandLine.getProduct());
        assertEquals(quantity, commandLine.getQuantity());
    }

    /**
     * Test l'ajout d'un produit dans le panier d'un client mais avec une quantité nulle ou négative.
     */
    @Test
    public void testAddProductInvalidQuantity() throws Exception {
        Client client = clientRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        Random random = new Random();
        Product product = productRepository.findById(random.nextLong(1, 10)).orElseThrow(NoSuchElementException::new);
        int quantity = 0;

        try {
            cartService.addProduct(client.getId(), product.getId(), quantity);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Test l'ajout d'un produit dans le panier d'un client mais les quantités demandées dépassent le stock du produit.
     */
    @Test
    public void testAddProductStockOverflow() throws Exception {
        Client client = clientRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        Random random = new Random();
        Product product = productRepository.findById(random.nextLong(1, 10)).orElseThrow(NoSuchElementException::new);
        int quantity = product.getStock() + 1;

        try {
            cartService.addProduct(client.getId(), product.getId(), quantity);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Test de l'ajout d'un produit deux fois dans le panier d'un client.
     */
    @Test
    public void testAddProductTwice() throws Exception {
        Client client = clientRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        Product product = productRepository.findById(1L).orElseThrow(NoSuchElementException::new);

        cartService.addProduct(client.getId(), product.getId());

        try {
            cartService.addProduct(client.getId(), product.getId());
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    /**
     * Test le paiement d'un panier par le client propriétaire du panier.
     */
    @Test
    public void testPayValid() throws Exception {
        final int quantity = 1;
        final List<Integer> oldStocks = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            Product product = productRepository.findById(i).orElseThrow(NoSuchElementException::new);
            cartService.addProduct(CLIENT_ID, i);
            cartService.setProductQuantity(CLIENT_ID, i, quantity);
            oldStocks.add(product.getStock());
        }

        Client client = clientRepository.findById(CLIENT_ID).orElseThrow(NoSuchElementException::new);
        cartService.pay(client.getCurrentCart());

        for (long i = 1; i <= oldStocks.size(); i++) {
            Product product = productRepository.findById(i).orElseThrow(NoSuchElementException::new);
            assertEquals(product.getStock(), oldStocks.get((int) i - 1) - quantity);
        }
    }

    /**
     * Test la suppression d'un produit du panier d'un client.
     */
    @Test
    public void testRemoveProduct() throws Exception {
        cartService.addProduct(CLIENT_ID, 1L);
        cartService.removeProduct(CLIENT_ID, 1L);

        Client client = clientRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        assertTrue(client.getCurrentCart().getCommandLines().isEmpty());
    }

}
