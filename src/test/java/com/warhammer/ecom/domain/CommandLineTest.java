package com.warhammer.ecom.domain;

import static com.warhammer.ecom.domain.CartTestSamples.*;
import static com.warhammer.ecom.domain.CommandLineTestSamples.*;
import static com.warhammer.ecom.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warhammer.ecom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommandLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommandLine.class);
        CommandLine commandLine1 = getCommandLineSample1();
        CommandLine commandLine2 = new CommandLine();
        assertThat(commandLine1).isNotEqualTo(commandLine2);

        commandLine2.setId(commandLine1.getId());
        assertThat(commandLine1).isEqualTo(commandLine2);

        commandLine2 = getCommandLineSample2();
        assertThat(commandLine1).isNotEqualTo(commandLine2);
    }

    @Test
    void productTest() {
        CommandLine commandLine = getCommandLineRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        commandLine.setProduct(productBack);
        assertThat(commandLine.getProduct()).isEqualTo(productBack);

        commandLine.product(null);
        assertThat(commandLine.getProduct()).isNull();
    }

    @Test
    void cartTest() {
        CommandLine commandLine = getCommandLineRandomSampleGenerator();
        Cart cartBack = getCartRandomSampleGenerator();

        commandLine.setCart(cartBack);
        assertThat(commandLine.getCart()).isEqualTo(cartBack);

        commandLine.cart(null);
        assertThat(commandLine.getCart()).isNull();
    }
}
