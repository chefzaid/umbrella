package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActivityReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ActivityReport getActivityReportSample1() {
        return new ActivityReport().id(1L).comments("comments1");
    }

    public static ActivityReport getActivityReportSample2() {
        return new ActivityReport().id(2L).comments("comments2");
    }

    public static ActivityReport getActivityReportRandomSampleGenerator() {
        return new ActivityReport().id(longCount.incrementAndGet()).comments(UUID.randomUUID().toString());
    }
}
