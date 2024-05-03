package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Employee getEmployeeSample1() {
        return new Employee()
            .id(1L)
            .employeeNumber(1L)
            .birthPlace("birthPlace1")
            .nationality("nationality1")
            .dependentChildrenNumber(1L)
            .socialSecurityNumber("socialSecurityNumber1")
            .iban("iban1")
            .bicSwift("bicSwift1");
    }

    public static Employee getEmployeeSample2() {
        return new Employee()
            .id(2L)
            .employeeNumber(2L)
            .birthPlace("birthPlace2")
            .nationality("nationality2")
            .dependentChildrenNumber(2L)
            .socialSecurityNumber("socialSecurityNumber2")
            .iban("iban2")
            .bicSwift("bicSwift2");
    }

    public static Employee getEmployeeRandomSampleGenerator() {
        return new Employee()
            .id(longCount.incrementAndGet())
            .employeeNumber(longCount.incrementAndGet())
            .birthPlace(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .dependentChildrenNumber(longCount.incrementAndGet())
            .socialSecurityNumber(UUID.randomUUID().toString())
            .iban(UUID.randomUUID().toString())
            .bicSwift(UUID.randomUUID().toString());
    }
}
