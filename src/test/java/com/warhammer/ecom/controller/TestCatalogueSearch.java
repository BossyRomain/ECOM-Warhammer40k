package com.warhammer.ecom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.controller.dto.ProductCatalogueDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Regroupe l'ensemble des tests de la recherche de produits dans le catalogue.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestCatalogueSearch {

    private final String SEARCH_ROUTE = "/api/products/search?query=";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Recherche ne correspondant à aucun produit.
     */
    @Test
    public void testNoProductFound() throws Exception {
        final String SEARCH = "aeifhgtjdhnok;ûiloh ggggfd";
        MvcResult mvcResult = mockMvc.perform(get(SEARCH_ROUTE + SEARCH)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });
        int totalElements = objectMapper.convertValue(node.get("totalElements"), Integer.class);

        assertEquals(0, totalElements);
        assertThat(products).isEmpty();
    }

    /**
     * Recherche d'un seul produit.
     */
    @Test
    public void testSingleProduct() throws Exception {
        final String SEARCH = "Imotekh the Stormlord";
        MvcResult mvcResult = mockMvc.perform(get(SEARCH_ROUTE + SEARCH)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });
        int totalElements = objectMapper.convertValue(node.get("totalElements"), Integer.class);

        assertEquals(1, totalElements);
        assertEquals(SEARCH, products.get(0).getName());
    }

    /**
     * Recherche de plusieurs produits.
     */
    @Test
    public void testMutilpleProducts() throws Exception {
        final String SEARCH = "Cadian";
        MvcResult mvcResult = mockMvc.perform(get(SEARCH_ROUTE + SEARCH)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });
        int totalElements = objectMapper.convertValue(node.get("totalElements"), Integer.class);

        assertTrue(totalElements > 1);
        for (ProductCatalogueDTO product : products) {
            assertTrue(product.getName().contains(SEARCH));
        }
    }

}
