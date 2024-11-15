package com.warhammer.ecom.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestCartService {

    /**
     * Test l'ajout d'un produit dans le panier d'un client.
     * @throws Exception an exception
     */
    @Test
    public void testAddProduct() throws Exception {

    }

    /**
     * Test la modification des quantités pour un produit dans le panier d'un client.
     * @throws Exception an exception
     */
    @Test
    public void testSetProductQuantity() throws Exception {

    }

    /**
     * Test la suppression d'un produit dans le panier d'un client.
     * @throws Exception an exception
     */
    @Test
    public void testRemoveProduct() throws Exception {

    }
}
