package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvoiceLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvoiceLine getInvoiceLineSample1() {
        return new InvoiceLine().id(1L).label("label1").description("description1");
    }

    public static InvoiceLine getInvoiceLineSample2() {
        return new InvoiceLine().id(2L).label("label2").description("description2");
    }

    public static InvoiceLine getInvoiceLineRandomSampleGenerator() {
        return new InvoiceLine()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
