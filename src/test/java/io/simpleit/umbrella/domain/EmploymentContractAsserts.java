package io.simpleit.umbrella.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EmploymentContractAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmploymentContractAllPropertiesEquals(EmploymentContract expected, EmploymentContract actual) {
        assertEmploymentContractAutoGeneratedPropertiesEquals(expected, actual);
        assertEmploymentContractAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmploymentContractAllUpdatablePropertiesEquals(EmploymentContract expected, EmploymentContract actual) {
        assertEmploymentContractUpdatableFieldsEquals(expected, actual);
        assertEmploymentContractUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmploymentContractAutoGeneratedPropertiesEquals(EmploymentContract expected, EmploymentContract actual) {
        assertThat(expected)
            .as("Verify EmploymentContract auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmploymentContractUpdatableFieldsEquals(EmploymentContract expected, EmploymentContract actual) {
        assertThat(expected)
            .as("Verify EmploymentContract relevant properties")
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getJobTitle()).as("check jobTitle").isEqualTo(actual.getJobTitle()))
            .satisfies(e -> assertThat(e.getHireDate()).as("check hireDate").isEqualTo(actual.getHireDate()))
            .satisfies(e -> assertThat(e.getSalary()).as("check salary").isEqualTo(actual.getSalary()))
            .satisfies(e -> assertThat(e.getYearlyWorkDays()).as("check yearlyWorkDays").isEqualTo(actual.getYearlyWorkDays()))
            .satisfies(e -> assertThat(e.getSignedByEmployer()).as("check signedByEmployer").isEqualTo(actual.getSignedByEmployer()))
            .satisfies(e -> assertThat(e.getSignedByEmployee()).as("check signedByEmployee").isEqualTo(actual.getSignedByEmployee()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEmploymentContractUpdatableRelationshipsEquals(EmploymentContract expected, EmploymentContract actual) {
        assertThat(expected)
            .as("Verify EmploymentContract relationships")
            .satisfies(e -> assertThat(e.getForumula()).as("check forumula").isEqualTo(actual.getForumula()))
            .satisfies(e -> assertThat(e.getEmploymentContract()).as("check employmentContract").isEqualTo(actual.getEmploymentContract()));
    }
}
