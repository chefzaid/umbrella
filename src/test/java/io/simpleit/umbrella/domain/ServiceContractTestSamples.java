package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceContractTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceContract getServiceContractSample1() {
        return new ServiceContract().id(1L).serviceLabel("serviceLabel1").extensionTerms("extensionTerms1");
    }

    public static ServiceContract getServiceContractSample2() {
        return new ServiceContract().id(2L).serviceLabel("serviceLabel2").extensionTerms("extensionTerms2");
    }

    public static ServiceContract getServiceContractRandomSampleGenerator() {
        return new ServiceContract()
            .id(longCount.incrementAndGet())
            .serviceLabel(UUID.randomUUID().toString())
            .extensionTerms(UUID.randomUUID().toString());
    }
}
