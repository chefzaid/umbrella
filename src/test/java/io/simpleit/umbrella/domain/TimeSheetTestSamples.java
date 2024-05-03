package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TimeSheetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TimeSheet getTimeSheetSample1() {
        return new TimeSheet().id(1L).concernedMonth("concernedMonth1");
    }

    public static TimeSheet getTimeSheetSample2() {
        return new TimeSheet().id(2L).concernedMonth("concernedMonth2");
    }

    public static TimeSheet getTimeSheetRandomSampleGenerator() {
        return new TimeSheet().id(longCount.incrementAndGet()).concernedMonth(UUID.randomUUID().toString());
    }
}
