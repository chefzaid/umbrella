package io.simpleit.umbrella.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParameterAllPropertiesEquals(Parameter expected, Parameter actual) {
        assertParameterAutoGeneratedPropertiesEquals(expected, actual);
        assertParameterAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParameterAllUpdatablePropertiesEquals(Parameter expected, Parameter actual) {
        assertParameterUpdatableFieldsEquals(expected, actual);
        assertParameterUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParameterAutoGeneratedPropertiesEquals(Parameter expected, Parameter actual) {
        assertThat(expected)
            .as("Verify Parameter auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParameterUpdatableFieldsEquals(Parameter expected, Parameter actual) {
        assertThat(expected)
            .as("Verify Parameter relevant properties")
            .satisfies(e -> assertThat(e.getLabel()).as("check label").isEqualTo(actual.getLabel()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertParameterUpdatableRelationshipsEquals(Parameter expected, Parameter actual) {
        assertThat(expected)
            .as("Verify Parameter relationships")
            .satisfies(e -> assertThat(e.getAppUser()).as("check appUser").isEqualTo(actual.getAppUser()))
            .satisfies(e -> assertThat(e.getEnterprise()).as("check enterprise").isEqualTo(actual.getEnterprise()));
    }
}