package com.warhammer.ecom.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AllegianceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Allegiance getAllegianceSample1() {
        return new Allegiance().id(1L);
    }

    public static Allegiance getAllegianceSample2() {
        return new Allegiance().id(2L);
    }

    public static Allegiance getAllegianceRandomSampleGenerator() {
        return new Allegiance().id(longCount.incrementAndGet());
    }
}
