package com.warhammer.ecom.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CommandLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CommandLine getCommandLineSample1() {
        return new CommandLine().id(1L).quanity(1);
    }

    public static CommandLine getCommandLineSample2() {
        return new CommandLine().id(2L).quanity(2);
    }

    public static CommandLine getCommandLineRandomSampleGenerator() {
        return new CommandLine().id(longCount.incrementAndGet()).quanity(intCount.incrementAndGet());
    }
}
