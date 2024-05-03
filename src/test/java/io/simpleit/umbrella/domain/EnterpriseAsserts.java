package io.simpleit.umbrella.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EnterpriseAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseAllPropertiesEquals(Enterprise expected, Enterprise actual) {
        assertEnterpriseAutoGeneratedPropertiesEquals(expected, actual);
        assertEnterpriseAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseAllUpdatablePropertiesEquals(Enterprise expected, Enterprise actual) {
        assertEnterpriseUpdatableFieldsEquals(expected, actual);
        assertEnterpriseUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseAutoGeneratedPropertiesEquals(Enterprise expected, Enterprise actual) {
        assertThat(expected)
            .as("Verify Enterprise auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseUpdatableFieldsEquals(Enterprise expected, Enterprise actual) {
        assertThat(expected)
            .as("Verify Enterprise relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getCompanyStatus()).as("check companyStatus").isEqualTo(actual.getCompanyStatus()))
            .satisfies(e -> assertThat(e.getLogo()).as("check logo").isEqualTo(actual.getLogo()))
            .satisfies(e -> assertThat(e.getLogoContentType()).as("check logo contenty type").isEqualTo(actual.getLogoContentType()))
            .satisfies(e -> assertThat(e.getSiret()).as("check siret").isEqualTo(actual.getSiret()))
            .satisfies(e -> assertThat(e.getSiren()).as("check siren").isEqualTo(actual.getSiren()))
            .satisfies(e -> assertThat(e.getSalesTaxNumber()).as("check salesTaxNumber").isEqualTo(actual.getSalesTaxNumber()))
            .satisfies(e -> assertThat(e.getIban()).as("check iban").isEqualTo(actual.getIban()))
            .satisfies(e -> assertThat(e.getBicSwift()).as("check bicSwift").isEqualTo(actual.getBicSwift()))
            .satisfies(e -> assertThat(e.getWebsite()).as("check website").isEqualTo(actual.getWebsite()))
            .satisfies(
                e -> assertThat(e.getDefaultInvoiceTerms()).as("check defaultInvoiceTerms").isEqualTo(actual.getDefaultInvoiceTerms())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseUpdatableRelationshipsEquals(Enterprise expected, Enterprise actual) {}
}
