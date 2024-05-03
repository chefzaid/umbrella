package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.EmploymentContractTestSamples.*;
import static io.simpleit.umbrella.domain.EmploymentContractTestSamples.*;
import static io.simpleit.umbrella.domain.FormulaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmploymentContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmploymentContract.class);
        EmploymentContract employmentContract1 = getEmploymentContractSample1();
        EmploymentContract employmentContract2 = new EmploymentContract();
        assertThat(employmentContract1).isNotEqualTo(employmentContract2);

        employmentContract2.setId(employmentContract1.getId());
        assertThat(employmentContract1).isEqualTo(employmentContract2);

        employmentContract2 = getEmploymentContractSample2();
        assertThat(employmentContract1).isNotEqualTo(employmentContract2);
    }

    @Test
    void forumulaTest() throws Exception {
        EmploymentContract employmentContract = getEmploymentContractRandomSampleGenerator();
        Formula formulaBack = getFormulaRandomSampleGenerator();

        employmentContract.setForumula(formulaBack);
        assertThat(employmentContract.getForumula()).isEqualTo(formulaBack);

        employmentContract.forumula(null);
        assertThat(employmentContract.getForumula()).isNull();
    }

    @Test
    void documentsTest() throws Exception {
        EmploymentContract employmentContract = getEmploymentContractRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        employmentContract.addDocuments(documentBack);
        assertThat(employmentContract.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getEmploymentContract()).isEqualTo(employmentContract);

        employmentContract.removeDocuments(documentBack);
        assertThat(employmentContract.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getEmploymentContract()).isNull();

        employmentContract.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(employmentContract.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getEmploymentContract()).isEqualTo(employmentContract);

        employmentContract.setDocuments(new HashSet<>());
        assertThat(employmentContract.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getEmploymentContract()).isNull();
    }

    @Test
    void amendmentsTest() throws Exception {
        EmploymentContract employmentContract = getEmploymentContractRandomSampleGenerator();
        EmploymentContract employmentContractBack = getEmploymentContractRandomSampleGenerator();

        employmentContract.addAmendments(employmentContractBack);
        assertThat(employmentContract.getAmendments()).containsOnly(employmentContractBack);
        assertThat(employmentContractBack.getEmploymentContract()).isEqualTo(employmentContract);

        employmentContract.removeAmendments(employmentContractBack);
        assertThat(employmentContract.getAmendments()).doesNotContain(employmentContractBack);
        assertThat(employmentContractBack.getEmploymentContract()).isNull();

        employmentContract.amendments(new HashSet<>(Set.of(employmentContractBack)));
        assertThat(employmentContract.getAmendments()).containsOnly(employmentContractBack);
        assertThat(employmentContractBack.getEmploymentContract()).isEqualTo(employmentContract);

        employmentContract.setAmendments(new HashSet<>());
        assertThat(employmentContract.getAmendments()).doesNotContain(employmentContractBack);
        assertThat(employmentContractBack.getEmploymentContract()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        EmploymentContract employmentContract = getEmploymentContractRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employmentContract.setEmployee(employeeBack);
        assertThat(employmentContract.getEmployee()).isEqualTo(employeeBack);
        assertThat(employeeBack.getContract()).isEqualTo(employmentContract);

        employmentContract.employee(null);
        assertThat(employmentContract.getEmployee()).isNull();
        assertThat(employeeBack.getContract()).isNull();
    }

    @Test
    void employmentContractTest() throws Exception {
        EmploymentContract employmentContract = getEmploymentContractRandomSampleGenerator();
        EmploymentContract employmentContractBack = getEmploymentContractRandomSampleGenerator();

        employmentContract.setEmploymentContract(employmentContractBack);
        assertThat(employmentContract.getEmploymentContract()).isEqualTo(employmentContractBack);

        employmentContract.employmentContract(null);
        assertThat(employmentContract.getEmploymentContract()).isNull();
    }
}
