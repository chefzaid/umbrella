package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseNoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExpenseNote getExpenseNoteSample1() {
        return new ExpenseNote().id(1L).label("label1").concernedMonth("concernedMonth1");
    }

    public static ExpenseNote getExpenseNoteSample2() {
        return new ExpenseNote().id(2L).label("label2").concernedMonth("concernedMonth2");
    }

    public static ExpenseNote getExpenseNoteRandomSampleGenerator() {
        return new ExpenseNote()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .concernedMonth(UUID.randomUUID().toString());
    }
}
