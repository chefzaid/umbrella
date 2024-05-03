package io.simpleit.umbrella.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAllPropertiesEquals(Document expected, Document actual) {
        assertDocumentAutoGeneratedPropertiesEquals(expected, actual);
        assertDocumentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAllUpdatablePropertiesEquals(Document expected, Document actual) {
        assertDocumentUpdatableFieldsEquals(expected, actual);
        assertDocumentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAutoGeneratedPropertiesEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentUpdatableFieldsEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document relevant properties")
            .satisfies(e -> assertThat(e.getLabel()).as("check label").isEqualTo(actual.getLabel()))
            .satisfies(e -> assertThat(e.getUploadDate()).as("check uploadDate").isEqualTo(actual.getUploadDate()))
            .satisfies(e -> assertThat(e.getIssuingDate()).as("check issuingDate").isEqualTo(actual.getIssuingDate()))
            .satisfies(e -> assertThat(e.getExpirationDate()).as("check expirationDate").isEqualTo(actual.getExpirationDate()))
            .satisfies(e -> assertThat(e.getFile()).as("check file").isEqualTo(actual.getFile()))
            .satisfies(e -> assertThat(e.getFileContentType()).as("check file contenty type").isEqualTo(actual.getFileContentType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentUpdatableRelationshipsEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document relationships")
            .satisfies(e -> assertThat(e.getDocumentType()).as("check documentType").isEqualTo(actual.getDocumentType()))
            .satisfies(e -> assertThat(e.getEmployee()).as("check employee").isEqualTo(actual.getEmployee()))
            .satisfies(e -> assertThat(e.getEmploymentContract()).as("check employmentContract").isEqualTo(actual.getEmploymentContract()))
            .satisfies(e -> assertThat(e.getServiceContract()).as("check serviceContract").isEqualTo(actual.getServiceContract()))
            .satisfies(e -> assertThat(e.getEnterprise()).as("check enterprise").isEqualTo(actual.getEnterprise()));
    }
}
