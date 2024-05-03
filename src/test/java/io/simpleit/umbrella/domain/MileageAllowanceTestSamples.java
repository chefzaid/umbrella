package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MileageAllowanceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MileageAllowance getMileageAllowanceSample1() {
        return new MileageAllowance().id(1L).multiplier(1L);
    }

    public static MileageAllowance getMileageAllowanceSample2() {
        return new MileageAllowance().id(2L).multiplier(2L);
    }

    public static MileageAllowance getMileageAllowanceRandomSampleGenerator() {
        return new MileageAllowance().id(longCount.incrementAndGet()).multiplier(longCount.incrementAndGet());
    }
}
