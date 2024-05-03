package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExpenseType getExpenseTypeSample1() {
        return new ExpenseType().id(1L).label("label1").description("description1");
    }

    public static ExpenseType getExpenseTypeSample2() {
        return new ExpenseType().id(2L).label("label2").description("description2");
    }

    public static ExpenseType getExpenseTypeRandomSampleGenerator() {
        return new ExpenseType()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
