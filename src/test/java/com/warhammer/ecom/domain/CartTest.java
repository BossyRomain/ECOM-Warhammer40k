package com.warhammer.ecom.domain;

import static com.warhammer.ecom.domain.CartTestSamples.*;
import static com.warhammer.ecom.domain.ClientTestSamples.*;
import static com.warhammer.ecom.domain.CommandLineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warhammer.ecom.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cart.class);
        Cart cart1 = getCartSample1();
        Cart cart2 = new Cart();
        assertThat(cart1).isNotEqualTo(cart2);

        cart2.setId(cart1.getId());
        assertThat(cart1).isEqualTo(cart2);

        cart2 = getCartSample2();
        assertThat(cart1).isNotEqualTo(cart2);
    }

    @Test
    void commandLinesTest() {
        Cart cart = getCartRandomSampleGenerator();
        CommandLine commandLineBack = getCommandLineRandomSampleGenerator();

        cart.addCommandLines(commandLineBack);
        assertThat(cart.getCommandLines()).containsOnly(commandLineBack);
        assertThat(commandLineBack.getCart()).isEqualTo(cart);

        cart.removeCommandLines(commandLineBack);
        assertThat(cart.getCommandLines()).doesNotContain(commandLineBack);
        assertThat(commandLineBack.getCart()).isNull();

        cart.commandLines(new HashSet<>(Set.of(commandLineBack)));
        assertThat(cart.getCommandLines()).containsOnly(commandLineBack);
        assertThat(commandLineBack.getCart()).isEqualTo(cart);

        cart.setCommandLines(new HashSet<>());
        assertThat(cart.getCommandLines()).doesNotContain(commandLineBack);
        assertThat(commandLineBack.getCart()).isNull();
    }

    @Test
    void clientTest() {
        Cart cart = getCartRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        cart.setClient(clientBack);
        assertThat(cart.getClient()).isEqualTo(clientBack);

        cart.client(null);
        assertThat(cart.getClient()).isNull();
    }
}
