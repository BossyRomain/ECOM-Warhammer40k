package com.warhammer.ecom.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestProductController {

    /**
     * Test l'accès au catalogue des produits.
     * @throws Exception an exception
     */
    @Test
    public void testCatalogue() throws Exception {

    }

    /**
     * Test l'accès aux détails d'un produit.
     * @throws Exception an exception
     */
    @Test
    public void testGetProduct() throws Exception {

    }
}
