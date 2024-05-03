package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmploymentContractTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmploymentContract getEmploymentContractSample1() {
        return new EmploymentContract().id(1L).jobTitle("jobTitle1").yearlyWorkDays(1L);
    }

    public static EmploymentContract getEmploymentContractSample2() {
        return new EmploymentContract().id(2L).jobTitle("jobTitle2").yearlyWorkDays(2L);
    }

    public static EmploymentContract getEmploymentContractRandomSampleGenerator() {
        return new EmploymentContract()
            .id(longCount.incrementAndGet())
            .jobTitle(UUID.randomUUID().toString())
            .yearlyWorkDays(longCount.incrementAndGet());
    }
}
