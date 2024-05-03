package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FormulaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Formula getFormulaSample1() {
        return new Formula().id(1L).label("label1");
    }

    public static Formula getFormulaSample2() {
        return new Formula().id(2L).label("label2");
    }

    public static Formula getFormulaRandomSampleGenerator() {
        return new Formula().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString());
    }
}
