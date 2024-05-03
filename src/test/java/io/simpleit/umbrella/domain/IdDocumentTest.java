package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.IdDocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdDocument.class);
        IdDocument idDocument1 = getIdDocumentSample1();
        IdDocument idDocument2 = new IdDocument();
        assertThat(idDocument1).isNotEqualTo(idDocument2);

        idDocument2.setId(idDocument1.getId());
        assertThat(idDocument1).isEqualTo(idDocument2);

        idDocument2 = getIdDocumentSample2();
        assertThat(idDocument1).isNotEqualTo(idDocument2);
    }

    @Test
    void documentTest() throws Exception {
        IdDocument idDocument = getIdDocumentRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        idDocument.setDocument(documentBack);
        assertThat(idDocument.getDocument()).isEqualTo(documentBack);

        idDocument.document(null);
        assertThat(idDocument.getDocument()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        IdDocument idDocument = getIdDocumentRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        idDocument.setEmployee(employeeBack);
        assertThat(idDocument.getEmployee()).isEqualTo(employeeBack);
        assertThat(employeeBack.getIdDocument()).isEqualTo(idDocument);

        idDocument.employee(null);
        assertThat(idDocument.getEmployee()).isNull();
        assertThat(employeeBack.getIdDocument()).isNull();
    }
}
