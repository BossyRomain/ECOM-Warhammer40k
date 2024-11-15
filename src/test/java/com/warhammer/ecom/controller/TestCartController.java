package com.warhammer.ecom.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestCartController {

    /**
     * Test l'ajout de produits dans le panier d'un client.
     * @throws Exception an exception
     */
    @Test
    public void testAddProduct() throws Exception {

    }

    /**
     * Test le retrait de produits dans le panier d'un client.
     * @throws Exception an exception
     */
    @Test
    public void testRemoveProduct() throws Exception {

    }

    /**
     * Test la modification de la quantité d'un produit dans le panier d'un client.
     * @throws Exception an exception
     */
    @Test
    public void testSetProductQuantity() throws Exception {

    }
}
