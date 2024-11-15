package com.warhammer.ecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warhammer.ecom.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestClientController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Test la création d'un compte client.
     * @throws Exception an exception
     */
    @Test
    public void testSignUp() throws Exception {
    }

    /**
     * Test la connection d'un client à son compte.
     * @throws Exception an exception
     */
    @Test
    public void testLogin() throws Exception {
    }

    /**
     * Test la suppression d'un compte client.
     * @throws Exception an exception
     */
    @Test
    public void testDeleteClient() throws Exception {
    }
}
