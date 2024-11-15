package com.warhammer.ecom;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestRouteSecurity {

    /**
     * Test que les routes publiques sont bien accessibles à tout les utilisateurs et sans avoir besoin d'être connecté.
     * @throws Exception an Exception
     */
    @Test
    public void testPublicRoutes() throws Exception {

    }

    /**
     * Test que les routes relatives aux informations d'un client (ajout produit panier,
     * suppression du compte, etc...) ne sont possibles que par le propriétaire du compte et
     * les administrateurs.
     * @throws Exception an exception
     */
    @Test
    public void testClientRoutes() throws Exception {

    }

    /**
     * Test que certaines routes ne sont accessibles qu'aux adimnistrateurs.
     * @throws Exception an exception
     */
    @Test
    public void testAdminRoutes() throws Exception {

    }
}
