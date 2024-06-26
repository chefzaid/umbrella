package io.simpleit.umbrella.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeAllPropertiesEquals(Employee expected, Employee actual) {
        assertEmployeeAutoGeneratedPropertiesEquals(expected, actual);
        assertEmployeeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeAllUpdatablePropertiesEquals(Employee expected, Employee actual) {
        assertEmployeeUpdatableFieldsEquals(expected, actual);
        assertEmployeeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeAutoGeneratedPropertiesEquals(Employee expected, Employee actual) {
        assertThat(expected)
            .as("Verify Employee auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeUpdatableFieldsEquals(Employee expected, Employee actual) {
        assertThat(expected)
            .as("Verify Employee relevant properties")
            .satisfies(e -> assertThat(e.getEmployeeNumber()).as("check employeeNumber").isEqualTo(actual.getEmployeeNumber()))
            .satisfies(e -> assertThat(e.getBirthDate()).as("check birthDate").isEqualTo(actual.getBirthDate()))
            .satisfies(e -> assertThat(e.getBirthPlace()).as("check birthPlace").isEqualTo(actual.getBirthPlace()))
            .satisfies(e -> assertThat(e.getNationality()).as("check nationality").isEqualTo(actual.getNationality()))
            .satisfies(e -> assertThat(e.getGender()).as("check gender").isEqualTo(actual.getGender()))
            .satisfies(e -> assertThat(e.getMaritalStatus()).as("check maritalStatus").isEqualTo(actual.getMaritalStatus()))
            .satisfies(
                e ->
                    assertThat(e.getDependentChildrenNumber())
                        .as("check dependentChildrenNumber")
                        .isEqualTo(actual.getDependentChildrenNumber())
            )
            .satisfies(
                e -> assertThat(e.getSocialSecurityNumber()).as("check socialSecurityNumber").isEqualTo(actual.getSocialSecurityNumber())
            )
            .satisfies(e -> assertThat(e.getIban()).as("check iban").isEqualTo(actual.getIban()))
            .satisfies(e -> assertThat(e.getBicSwift()).as("check bicSwift").isEqualTo(actual.getBicSwift()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmployeeUpdatableRelationshipsEquals(Employee expected, Employee actual) {
        assertThat(expected)
            .as("Verify Employee relationships")
            .satisfies(e -> assertThat(e.getUser()).as("check user").isEqualTo(actual.getUser()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getContract()).as("check contract").isEqualTo(actual.getContract()))
            .satisfies(e -> assertThat(e.getIdDocument()).as("check idDocument").isEqualTo(actual.getIdDocument()))
            .satisfies(e -> assertThat(e.getWallet()).as("check wallet").isEqualTo(actual.getWallet()))
            .satisfies(e -> assertThat(e.getManager()).as("check manager").isEqualTo(actual.getManager()));
    }
}
