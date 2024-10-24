package com.warhammer.ecom.domain;

import static com.warhammer.ecom.domain.AllegianceTestSamples.*;
import static com.warhammer.ecom.domain.CommandLineTestSamples.*;
import static com.warhammer.ecom.domain.ImageTestSamples.*;
import static com.warhammer.ecom.domain.ProductImageTestSamples.*;
import static com.warhammer.ecom.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warhammer.ecom.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void imageCatalogueTest() {
        Product product = getProductRandomSampleGenerator();
        ProductImage productImageBack = getProductImageRandomSampleGenerator();

        product.setImageCatalogue(productImageBack);
        assertThat(product.getImageCatalogue()).isEqualTo(productImageBack);

        product.imageCatalogue(null);
        assertThat(product.getImageCatalogue()).isNull();
    }

    @Test
    void allegianceTest() {
        Product product = getProductRandomSampleGenerator();
        Allegiance allegianceBack = getAllegianceRandomSampleGenerator();

        product.setAllegiance(allegianceBack);
        assertThat(product.getAllegiance()).isEqualTo(allegianceBack);

        product.allegiance(null);
        assertThat(product.getAllegiance()).isNull();
    }

    @Test
    void imagesTest() {
        Product product = getProductRandomSampleGenerator();
        Image imageBack = getImageRandomSampleGenerator();

        product.addImages(imageBack);
        assertThat(product.getImages()).containsOnly(imageBack);
        assertThat(imageBack.getProduct()).isEqualTo(product);

        product.removeImages(imageBack);
        assertThat(product.getImages()).doesNotContain(imageBack);
        assertThat(imageBack.getProduct()).isNull();

        product.images(new HashSet<>(Set.of(imageBack)));
        assertThat(product.getImages()).containsOnly(imageBack);
        assertThat(imageBack.getProduct()).isEqualTo(product);

        product.setImages(new HashSet<>());
        assertThat(product.getImages()).doesNotContain(imageBack);
        assertThat(imageBack.getProduct()).isNull();
    }

    @Test
    void commandLineTest() {
        Product product = getProductRandomSampleGenerator();
        CommandLine commandLineBack = getCommandLineRandomSampleGenerator();

        product.setCommandLine(commandLineBack);
        assertThat(product.getCommandLine()).isEqualTo(commandLineBack);
        assertThat(commandLineBack.getProduct()).isEqualTo(product);

        product.commandLine(null);
        assertThat(product.getCommandLine()).isNull();
        assertThat(commandLineBack.getProduct()).isNull();
    }
}
