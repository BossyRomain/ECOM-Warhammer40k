package com.warhammer.ecom.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestClientService {

    /**
     * Test les différents moyens de retrouver un compte client :
     * - avec son id
     * - avec l'id du compte utilisateur associé
     * - avec l'email du compte
     * @throws Exception an exception
     */
    @Test
    public void testGetClient() throws Exception {

    }

    /**
     * Test la création d'un compte client.
     * @throws Exception an exception
     */
    @Test
    public void testCreateClient() throws Exception {

    }

    /**
     * Test la modification des informations d'un compte client.
     * @throws Exception an exception
     */
    @Test
    public void testUpdateClient() throws Exception {

    }

    /**
     * Test la suppression d'un compte client.
     * @throws Exception an exception
     */
    @Test
    public void testDeleteClient() throws Exception {

    }
}
