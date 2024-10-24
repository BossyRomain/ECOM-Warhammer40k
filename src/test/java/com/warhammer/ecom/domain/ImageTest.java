package com.warhammer.ecom.domain;

import static com.warhammer.ecom.domain.ImageTestSamples.*;
import static com.warhammer.ecom.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warhammer.ecom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Image.class);
        Image image1 = getImageSample1();
        Image image2 = new Image();
        assertThat(image1).isNotEqualTo(image2);

        image2.setId(image1.getId());
        assertThat(image1).isEqualTo(image2);

        image2 = getImageSample2();
        assertThat(image1).isNotEqualTo(image2);
    }

    @Test
    void productTest() {
        Image image = getImageRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        image.setProduct(productBack);
        assertThat(image.getProduct()).isEqualTo(productBack);

        image.product(null);
        assertThat(image.getProduct()).isNull();
    }
}
