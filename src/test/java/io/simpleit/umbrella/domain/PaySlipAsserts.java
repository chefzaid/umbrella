package io.simpleit.umbrella.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PaySlipAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaySlipAllPropertiesEquals(PaySlip expected, PaySlip actual) {
        assertPaySlipAutoGeneratedPropertiesEquals(expected, actual);
        assertPaySlipAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaySlipAllUpdatablePropertiesEquals(PaySlip expected, PaySlip actual) {
        assertPaySlipUpdatableFieldsEquals(expected, actual);
        assertPaySlipUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaySlipAutoGeneratedPropertiesEquals(PaySlip expected, PaySlip actual) {
        assertThat(expected)
            .as("Verify PaySlip auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaySlipUpdatableFieldsEquals(PaySlip expected, PaySlip actual) {
        assertThat(expected)
            .as("Verify PaySlip relevant properties")
            .satisfies(e -> assertThat(e.getSuperGrossSalary()).as("check superGrossSalary").isEqualTo(actual.getSuperGrossSalary()))
            .satisfies(e -> assertThat(e.getGrossSalary()).as("check grossSalary").isEqualTo(actual.getGrossSalary()))
            .satisfies(e -> assertThat(e.getNetSalary()).as("check netSalary").isEqualTo(actual.getNetSalary()))
            .satisfies(e -> assertThat(e.getTaxRate()).as("check taxRate").isEqualTo(actual.getTaxRate()))
            .satisfies(e -> assertThat(e.getAmountPaid()).as("check amountPaid").isEqualTo(actual.getAmountPaid()))
            .satisfies(e -> assertThat(e.getTotalExpenses()).as("check totalExpenses").isEqualTo(actual.getTotalExpenses()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaySlipUpdatableRelationshipsEquals(PaySlip expected, PaySlip actual) {
        assertThat(expected)
            .as("Verify PaySlip relationships")
            .satisfies(e -> assertThat(e.getDocument()).as("check document").isEqualTo(actual.getDocument()))
            .satisfies(e -> assertThat(e.getEmployee()).as("check employee").isEqualTo(actual.getEmployee()));
    }
}
