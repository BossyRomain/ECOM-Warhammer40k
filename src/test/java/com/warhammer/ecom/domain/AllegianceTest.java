package com.warhammer.ecom.domain;

import static com.warhammer.ecom.domain.AllegianceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.warhammer.ecom.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllegianceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Allegiance.class);
        Allegiance allegiance1 = getAllegianceSample1();
        Allegiance allegiance2 = new Allegiance();
        assertThat(allegiance1).isNotEqualTo(allegiance2);

        allegiance2.setId(allegiance1.getId());
        assertThat(allegiance1).isEqualTo(allegiance2);

        allegiance2 = getAllegianceSample2();
        assertThat(allegiance1).isNotEqualTo(allegiance2);
    }
}
