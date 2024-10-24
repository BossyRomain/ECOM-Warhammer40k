package com.warhammer.ecom.domain;

import static com.warhammer.ecom.domain.CartTestSamples.*;
import static com.warhammer.ecom.domain.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warhammer.ecom.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void cartsTest() {
        Client client = getClientRandomSampleGenerator();
        Cart cartBack = getCartRandomSampleGenerator();

        client.addCarts(cartBack);
        assertThat(client.getCarts()).containsOnly(cartBack);
        assertThat(cartBack.getClient()).isEqualTo(client);

        client.removeCarts(cartBack);
        assertThat(client.getCarts()).doesNotContain(cartBack);
        assertThat(cartBack.getClient()).isNull();

        client.carts(new HashSet<>(Set.of(cartBack)));
        assertThat(client.getCarts()).containsOnly(cartBack);
        assertThat(cartBack.getClient()).isEqualTo(client);

        client.setCarts(new HashSet<>());
        assertThat(client.getCarts()).doesNotContain(cartBack);
        assertThat(cartBack.getClient()).isNull();
    }
}
