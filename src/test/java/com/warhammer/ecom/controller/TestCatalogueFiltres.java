package com.warhammer.ecom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.controller.dto.ProductCatalogueDTO;
import com.warhammer.ecom.model.Faction;
import com.warhammer.ecom.model.Group;
import com.warhammer.ecom.model.Product;
import com.warhammer.ecom.model.ProductType;
import com.warhammer.ecom.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Regroupe l'ensemble des tests relatifs à l'accès au catalogue des produits en utilisant les filtres.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestCatalogueFiltres {

    private final String CATALOGUE_ROUTE = "/api/products/search";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    /**
     * Test que les produits du catalogue sont triés par ordre décroissant de date de sortie.
     */
    @Test
    public void testSort() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(CATALOGUE_ROUTE)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });

        Timestamp previousReleaseDate = productService.get(products.get(0).getId()).getReleaseDate();
        for (ProductCatalogueDTO p : products) {
            Timestamp currentReleaseDate = productService.get(p.getId()).getReleaseDate();
            if (currentReleaseDate.after(previousReleaseDate)) {
                fail();
            }
            previousReleaseDate = currentReleaseDate;
        }
    }

    /**
     * Test la pagination du catalogue.
     */
    @Test
    public void testPagination() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(CATALOGUE_ROUTE)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> pageOne = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });

        mvcResult = mockMvc.perform(get(CATALOGUE_ROUTE + "?page=1")).andExpect(status().isOk()).andReturn();
        response = mvcResult.getResponse().getContentAsString();
        node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> pageTwo = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });

        for (ProductCatalogueDTO p1 : pageOne) {
            for (ProductCatalogueDTO p2 : pageTwo) {
                assertNotEquals(p1.getId(), p2.getId());
            }
        }
    }

    /**
     * Test du filtre du prix.
     */
    @Test
    public void testPriceFilter() throws Exception {
        double minPrice = 60.0;
        double maxPrice = 200.0;
        MvcResult mvcResult = mockMvc.perform(get(CATALOGUE_ROUTE + "?minprice=" + minPrice + "&maxprice=" + maxPrice)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });

        for (ProductCatalogueDTO p : products) {
            assertTrue(p.getUnitPrice() >= minPrice);
            assertTrue(p.getUnitPrice() <= maxPrice);
        }
    }

    /**
     * Test du filtre par types de produits.
     */
    @Test
    public void testTypesFilter() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(CATALOGUE_ROUTE + "?type=dice,paint")).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });

        for (ProductCatalogueDTO p : products) {
            assertTrue(p.getProductType().equals(ProductType.DICE) || p.getProductType().equals(ProductType.PAINT));
        }
    }

    /**
     * Test du filtre par groupes de factions.
     */
    @Test
    public void testGroupsFilter() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(CATALOGUE_ROUTE + "?group=xenos")).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });

        for (ProductCatalogueDTO p : products) {
            Product product = productService.get(p.getId());
            assertNotNull(product.getAllegiance());
            assertEquals(Group.XENOS, product.getAllegiance().getGroup());
        }
    }

    /**
     * Test du filtre par factions.
     */
    @Test
    public void testFactionsFilter() throws Exception {
        final Faction[] factions = new Faction[]{Faction.ORKS, Faction.NECRONS, Faction.CHAOS_DAEMONS};
        MvcResult mvcResult = mockMvc.perform(get(CATALOGUE_ROUTE + "?faction=orks,necrons,chaos_daemons")).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });

        for (ProductCatalogueDTO p : products) {
            Product product = productService.get(p.getId());
            assertNotNull(product.getAllegiance());
            assertThat(factions).contains(product.getAllegiance().getFaction());
        }
    }

    /**
     * Test d'un scénario complexe, on cherche toutes les figurines et les livres de règles pour le groupe IMPERIUM et les factions
     * NECRONS, ELDARS et TYRANID avec un prix minimum de 30.5€, un prix maximal de 100.5€
     */
    @Test
    public void testScenario1() throws Exception {
        double minPrice = 30.5;
        double maxPrice = 100.5;
        final ProductType[] types = new ProductType[]{ProductType.FIGURINE, ProductType.RULES_AND_CODEX};
        final List<Group> groups = List.of(Group.IMPERIUM);
        final List<Faction> factions = List.of(Faction.ELDARS, Faction.NECRONS, Faction.CHAOS_DAEMONS);
        MvcResult mvcResult = mockMvc.perform(get(CATALOGUE_ROUTE + "?size=35" +
                "&type=" + Arrays.stream(types).map(type -> type.name().toLowerCase()).collect(Collectors.joining(",")) +
                "&minprice=" + minPrice +
                "&maxprice=" + maxPrice +
                "&group=" + groups.stream().map(group -> group.name().toLowerCase()).collect(Collectors.joining(",")) +
                "&faction=" + factions.stream().map(faction -> faction.name().toLowerCase()).collect(Collectors.joining(","))))
            .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        List<ProductCatalogueDTO> products = objectMapper.convertValue(node.get("content"), new TypeReference<List<ProductCatalogueDTO>>() {
        });

        for (ProductCatalogueDTO p : products) {
            Product product = productService.get(p.getId());
            assertNotNull(product.getAllegiance());
            assertTrue(p.getUnitPrice() >= minPrice);
            assertTrue(p.getUnitPrice() <= maxPrice);
            assertThat(types).contains(product.getProductType());
            assertNotNull(product.getAllegiance());
            assertTrue(
                groups.contains(product.getAllegiance().getGroup()) ||
                    factions.contains(product.getAllegiance().getFaction()));
        }
    }
}
