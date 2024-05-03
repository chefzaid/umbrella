package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contact getContactSample1() {
        return new Contact()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .jobTitle("jobTitle1");
    }

    public static Contact getContactSample2() {
        return new Contact()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .jobTitle("jobTitle2");
    }

    public static Contact getContactRandomSampleGenerator() {
        return new Contact()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .jobTitle(UUID.randomUUID().toString());
    }
}
