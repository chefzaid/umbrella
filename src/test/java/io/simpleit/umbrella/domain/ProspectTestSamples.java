package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProspectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Prospect getProspectSample1() {
        return new Prospect().id(1L).dailyRate(1L).monthlyExpenses(1L).expectedClient("expectedClient1").notes("notes1");
    }

    public static Prospect getProspectSample2() {
        return new Prospect().id(2L).dailyRate(2L).monthlyExpenses(2L).expectedClient("expectedClient2").notes("notes2");
    }

    public static Prospect getProspectRandomSampleGenerator() {
        return new Prospect()
            .id(longCount.incrementAndGet())
            .dailyRate(longCount.incrementAndGet())
            .monthlyExpenses(longCount.incrementAndGet())
            .expectedClient(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
