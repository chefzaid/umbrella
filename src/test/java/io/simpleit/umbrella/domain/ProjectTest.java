package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ClientTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.ExpenseTestSamples.*;
import static io.simpleit.umbrella.domain.InvoiceTestSamples.*;
import static io.simpleit.umbrella.domain.ProjectTestSamples.*;
import static io.simpleit.umbrella.domain.TimeSheetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void clientTest() throws Exception {
        Project project = getProjectRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        project.setClient(clientBack);
        assertThat(project.getClient()).isEqualTo(clientBack);

        project.client(null);
        assertThat(project.getClient()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        Project project = getProjectRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        project.setEmployee(employeeBack);
        assertThat(project.getEmployee()).isEqualTo(employeeBack);

        project.employee(null);
        assertThat(project.getEmployee()).isNull();
    }

    @Test
    void timeSheetTest() throws Exception {
        Project project = getProjectRandomSampleGenerator();
        TimeSheet timeSheetBack = getTimeSheetRandomSampleGenerator();

        project.setTimeSheet(timeSheetBack);
        assertThat(project.getTimeSheet()).isEqualTo(timeSheetBack);
        assertThat(timeSheetBack.getProject()).isEqualTo(project);

        project.timeSheet(null);
        assertThat(project.getTimeSheet()).isNull();
        assertThat(timeSheetBack.getProject()).isNull();
    }

    @Test
    void expenseTest() throws Exception {
        Project project = getProjectRandomSampleGenerator();
        Expense expenseBack = getExpenseRandomSampleGenerator();

        project.setExpense(expenseBack);
        assertThat(project.getExpense()).isEqualTo(expenseBack);
        assertThat(expenseBack.getProject()).isEqualTo(project);

        project.expense(null);
        assertThat(project.getExpense()).isNull();
        assertThat(expenseBack.getProject()).isNull();
    }

    @Test
    void invoiceTest() throws Exception {
        Project project = getProjectRandomSampleGenerator();
        Invoice invoiceBack = getInvoiceRandomSampleGenerator();

        project.setInvoice(invoiceBack);
        assertThat(project.getInvoice()).isEqualTo(invoiceBack);
        assertThat(invoiceBack.getPoject()).isEqualTo(project);

        project.invoice(null);
        assertThat(project.getInvoice()).isNull();
        assertThat(invoiceBack.getPoject()).isNull();
    }
}
