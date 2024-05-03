package io.simpleit.umbrella.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceAllPropertiesEquals(Invoice expected, Invoice actual) {
        assertInvoiceAutoGeneratedPropertiesEquals(expected, actual);
        assertInvoiceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceAllUpdatablePropertiesEquals(Invoice expected, Invoice actual) {
        assertInvoiceUpdatableFieldsEquals(expected, actual);
        assertInvoiceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceAutoGeneratedPropertiesEquals(Invoice expected, Invoice actual) {
        assertThat(expected)
            .as("Verify Invoice auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceUpdatableFieldsEquals(Invoice expected, Invoice actual) {
        assertThat(expected)
            .as("Verify Invoice relevant properties")
            .satisfies(e -> assertThat(e.getInvoiceNumber()).as("check invoiceNumber").isEqualTo(actual.getInvoiceNumber()))
            .satisfies(e -> assertThat(e.getLabel()).as("check label").isEqualTo(actual.getLabel()))
            .satisfies(e -> assertThat(e.getIssueDate()).as("check issueDate").isEqualTo(actual.getIssueDate()))
            .satisfies(e -> assertThat(e.getCurrency()).as("check currency").isEqualTo(actual.getCurrency()))
            .satisfies(e -> assertThat(e.getSalesTaxPct()).as("check salesTaxPct").isEqualTo(actual.getSalesTaxPct()))
            .satisfies(e -> assertThat(e.getTerms()).as("check terms").isEqualTo(actual.getTerms()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceUpdatableRelationshipsEquals(Invoice expected, Invoice actual) {
        assertThat(expected)
            .as("Verify Invoice relationships")
            .satisfies(e -> assertThat(e.getPoject()).as("check poject").isEqualTo(actual.getPoject()))
            .satisfies(e -> assertThat(e.getDocument()).as("check document").isEqualTo(actual.getDocument()));
    }
}
