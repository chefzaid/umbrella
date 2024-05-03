package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IdDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IdDocument getIdDocumentSample1() {
        return new IdDocument().id(1L).idNumber("idNumber1");
    }

    public static IdDocument getIdDocumentSample2() {
        return new IdDocument().id(2L).idNumber("idNumber2");
    }

    public static IdDocument getIdDocumentRandomSampleGenerator() {
        return new IdDocument().id(longCount.incrementAndGet()).idNumber(UUID.randomUUID().toString());
    }
}
