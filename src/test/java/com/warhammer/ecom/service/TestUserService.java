package com.warhammer.ecom.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestUserService {

    /**
     * Test les différents moyens de retrouver un compte utilisateur :
     * - avec son id
     * - avec l'id du compte client associé
     * - avec le nom d'utilisateur.
     * @throws Exception an exception
     */
    @Test
    public void testGetUser() throws Exception {

    }

    /**
     * Test la création d'un nouvel utilisateur (sans compte client attaché).
     * @throws Exception an exception
     */
    @Test
    public void testCreateUser() throws Exception {

    }

    /**
     * Test la modification des informations d'un utilisateur.
     * @throws Exception an exception
     */
    public void testUpdateUser() throws Exception {

    }

    /**
     * Test de la suppression d'un utilisateur.
     * Lors de la suppression d'un compte utilisateur, le compte client lié à ce compte est également supprimé.
     * @throws Exception an exception
     */
    @Test
    public void testDeleteUser() throws Exception {

    }
}
