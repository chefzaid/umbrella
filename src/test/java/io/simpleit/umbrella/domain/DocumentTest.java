package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.EmploymentContractTestSamples.*;
import static io.simpleit.umbrella.domain.EnterpriseTestSamples.*;
import static io.simpleit.umbrella.domain.ExpenseNoteTestSamples.*;
import static io.simpleit.umbrella.domain.IdDocumentTestSamples.*;
import static io.simpleit.umbrella.domain.InvoiceTestSamples.*;
import static io.simpleit.umbrella.domain.ParameterTestSamples.*;
import static io.simpleit.umbrella.domain.PaySlipTestSamples.*;
import static io.simpleit.umbrella.domain.ServiceContractTestSamples.*;
import static io.simpleit.umbrella.domain.TimeSheetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = getDocumentSample1();
        Document document2 = new Document();
        assertThat(document1).isNotEqualTo(document2);

        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);

        document2 = getDocumentSample2();
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    void documentTypeTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        Parameter parameterBack = getParameterRandomSampleGenerator();

        document.setDocumentType(parameterBack);
        assertThat(document.getDocumentType()).isEqualTo(parameterBack);

        document.documentType(null);
        assertThat(document.getDocumentType()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        document.setEmployee(employeeBack);
        assertThat(document.getEmployee()).isEqualTo(employeeBack);

        document.employee(null);
        assertThat(document.getEmployee()).isNull();
    }

    @Test
    void idDocumentTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        IdDocument idDocumentBack = getIdDocumentRandomSampleGenerator();

        document.setIdDocument(idDocumentBack);
        assertThat(document.getIdDocument()).isEqualTo(idDocumentBack);
        assertThat(idDocumentBack.getDocument()).isEqualTo(document);

        document.idDocument(null);
        assertThat(document.getIdDocument()).isNull();
        assertThat(idDocumentBack.getDocument()).isNull();
    }

    @Test
    void employmentContractTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        EmploymentContract employmentContractBack = getEmploymentContractRandomSampleGenerator();

        document.setEmploymentContract(employmentContractBack);
        assertThat(document.getEmploymentContract()).isEqualTo(employmentContractBack);

        document.employmentContract(null);
        assertThat(document.getEmploymentContract()).isNull();
    }

    @Test
    void serviceContractTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        ServiceContract serviceContractBack = getServiceContractRandomSampleGenerator();

        document.setServiceContract(serviceContractBack);
        assertThat(document.getServiceContract()).isEqualTo(serviceContractBack);

        document.serviceContract(null);
        assertThat(document.getServiceContract()).isNull();
    }

    @Test
    void timeSheetTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        TimeSheet timeSheetBack = getTimeSheetRandomSampleGenerator();

        document.setTimeSheet(timeSheetBack);
        assertThat(document.getTimeSheet()).isEqualTo(timeSheetBack);
        assertThat(timeSheetBack.getDocument()).isEqualTo(document);

        document.timeSheet(null);
        assertThat(document.getTimeSheet()).isNull();
        assertThat(timeSheetBack.getDocument()).isNull();
    }

    @Test
    void expenseNoteTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        ExpenseNote expenseNoteBack = getExpenseNoteRandomSampleGenerator();

        document.setExpenseNote(expenseNoteBack);
        assertThat(document.getExpenseNote()).isEqualTo(expenseNoteBack);
        assertThat(expenseNoteBack.getDocument()).isEqualTo(document);

        document.expenseNote(null);
        assertThat(document.getExpenseNote()).isNull();
        assertThat(expenseNoteBack.getDocument()).isNull();
    }

    @Test
    void invoiceTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        document.setInvoice(invoiceBack);
        assertThat(document.getInvoice()).isEqualTo(invoiceBack);
        assertThat(invoiceBack.getDocument()).isEqualTo(document);

        document.invoice(null);
        assertThat(document.getInvoice()).isNull();
        assertThat(invoiceBack.getDocument()).isNull();
    }

    @Test
    void paySlipTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        PaySlip paySlipBack = getPaySlipRandomSampleGenerator();

        document.setPaySlip(paySlipBack);
        assertThat(document.getPaySlip()).isEqualTo(paySlipBack);
        assertThat(paySlipBack.getDocument()).isEqualTo(document);

        document.paySlip(null);
        assertThat(document.getPaySlip()).isNull();
        assertThat(paySlipBack.getDocument()).isNull();
    }

    @Test
    void enterpriseTest() throws Exception {
        Document document = getDocumentRandomSampleGenerator();
        Enterprise enterpriseBack = getEnterpriseRandomSampleGenerator();

        document.setEnterprise(enterpriseBack);
        assertThat(document.getEnterprise()).isEqualTo(enterpriseBack);

        document.enterprise(null);
        assertThat(document.getEnterprise()).isNull();
    }
}
