package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ParameterGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ParameterGroup getParameterGroupSample1() {
        return new ParameterGroup().id(1L).label("label1").description("description1");
    }

    public static ParameterGroup getParameterGroupSample2() {
        return new ParameterGroup().id(2L).label("label2").description("description2");
    }

    public static ParameterGroup getParameterGroupRandomSampleGenerator() {
        return new ParameterGroup()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
