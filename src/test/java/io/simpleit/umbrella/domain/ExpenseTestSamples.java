package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Expense getExpenseSample1() {
        return new Expense().id(1L).label("label1").description("description1").currency("currency1").comment("comment1");
    }

    public static Expense getExpenseSample2() {
        return new Expense().id(2L).label("label2").description("description2").currency("currency2").comment("comment2");
    }

    public static Expense getExpenseRandomSampleGenerator() {
        return new Expense()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .comment(UUID.randomUUID().toString());
    }
}
