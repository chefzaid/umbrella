package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PaySlipTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaySlip getPaySlipSample1() {
        return new PaySlip().id(1L);
    }

    public static PaySlip getPaySlipSample2() {
        return new PaySlip().id(2L);
    }

    public static PaySlip getPaySlipRandomSampleGenerator() {
        return new PaySlip().id(longCount.incrementAndGet());
    }
}
