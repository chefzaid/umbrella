package io.simpleit.umbrella.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MileageAllowanceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMileageAllowanceAllPropertiesEquals(MileageAllowance expected, MileageAllowance actual) {
        assertMileageAllowanceAutoGeneratedPropertiesEquals(expected, actual);
        assertMileageAllowanceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMileageAllowanceAllUpdatablePropertiesEquals(MileageAllowance expected, MileageAllowance actual) {
        assertMileageAllowanceUpdatableFieldsEquals(expected, actual);
        assertMileageAllowanceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMileageAllowanceAutoGeneratedPropertiesEquals(MileageAllowance expected, MileageAllowance actual) {
        assertThat(expected)
            .as("Verify MileageAllowance auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMileageAllowanceUpdatableFieldsEquals(MileageAllowance expected, MileageAllowance actual) {
        assertThat(expected)
            .as("Verify MileageAllowance relevant properties")
            .satisfies(e -> assertThat(e.getMileage()).as("check mileage").isEqualTo(actual.getMileage()))
            .satisfies(e -> assertThat(e.getMultiplier()).as("check multiplier").isEqualTo(actual.getMultiplier()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMileageAllowanceUpdatableRelationshipsEquals(MileageAllowance expected, MileageAllowance actual) {}
}
