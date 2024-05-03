package io.simpleit.umbrella.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnterpriseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Enterprise getEnterpriseSample1() {
        return new Enterprise()
            .id(1L)
            .name("name1")
            .companyStatus("companyStatus1")
            .siret("siret1")
            .siren("siren1")
            .salesTaxNumber("salesTaxNumber1")
            .iban("iban1")
            .bicSwift("bicSwift1")
            .website("website1")
            .defaultInvoiceTerms("defaultInvoiceTerms1");
    }

    public static Enterprise getEnterpriseSample2() {
        return new Enterprise()
            .id(2L)
            .name("name2")
            .companyStatus("companyStatus2")
            .siret("siret2")
            .siren("siren2")
            .salesTaxNumber("salesTaxNumber2")
            .iban("iban2")
            .bicSwift("bicSwift2")
            .website("website2")
            .defaultInvoiceTerms("defaultInvoiceTerms2");
    }

    public static Enterprise getEnterpriseRandomSampleGenerator() {
        return new Enterprise()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .companyStatus(UUID.randomUUID().toString())
            .siret(UUID.randomUUID().toString())
            .siren(UUID.randomUUID().toString())
            .salesTaxNumber(UUID.randomUUID().toString())
            .iban(UUID.randomUUID().toString())
            .bicSwift(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .defaultInvoiceTerms(UUID.randomUUID().toString());
    }
}
