package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimeSheetLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TimeSheetLine getTimeSheetLineSample1() {
        return new TimeSheetLine().id(1L).comments("comments1");
    }

    public static TimeSheetLine getTimeSheetLineSample2() {
        return new TimeSheetLine().id(2L).comments("comments2");
    }

    public static TimeSheetLine getTimeSheetLineRandomSampleGenerator() {
        return new TimeSheetLine().id(longCount.incrementAndGet()).comments(UUID.randomUUID().toString());
    }
}
