package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ActivityReportTestSamples.*;
import static io.simpleit.umbrella.domain.AddressTestSamples.*;
import static io.simpleit.umbrella.domain.AppUserTestSamples.*;
import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.EmploymentContractTestSamples.*;
import static io.simpleit.umbrella.domain.ExpenseNoteTestSamples.*;
import static io.simpleit.umbrella.domain.IdDocumentTestSamples.*;
import static io.simpleit.umbrella.domain.PaySlipTestSamples.*;
import static io.simpleit.umbrella.domain.ProjectTestSamples.*;
import static io.simpleit.umbrella.domain.TimeSheetTestSamples.*;
import static io.simpleit.umbrella.domain.WalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = getEmployeeSample1();
        Employee employee2 = new Employee();
        assertThat(employee1).isNotEqualTo(employee2);

        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);

        employee2 = getEmployeeSample2();
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    void userTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        employee.setUser(appUserBack);
        assertThat(employee.getUser()).isEqualTo(appUserBack);

        employee.user(null);
        assertThat(employee.getUser()).isNull();
    }

    @Test
    void addressTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        employee.setAddress(addressBack);
        assertThat(employee.getAddress()).isEqualTo(addressBack);

        employee.address(null);
        assertThat(employee.getAddress()).isNull();
    }

    @Test
    void contractTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        EmploymentContract employmentContractBack = getEmploymentContractRandomSampleGenerator();

        employee.setContract(employmentContractBack);
        assertThat(employee.getContract()).isEqualTo(employmentContractBack);

        employee.contract(null);
        assertThat(employee.getContract()).isNull();
    }

    @Test
    void idDocumentTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        IdDocument idDocumentBack = getIdDocumentRandomSampleGenerator();

        employee.setIdDocument(idDocumentBack);
        assertThat(employee.getIdDocument()).isEqualTo(idDocumentBack);

        employee.idDocument(null);
        assertThat(employee.getIdDocument()).isNull();
    }

    @Test
    void walletTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Wallet walletBack = getWalletRandomSampleGenerator();

        employee.setWallet(walletBack);
        assertThat(employee.getWallet()).isEqualTo(walletBack);

        employee.wallet(null);
        assertThat(employee.getWallet()).isNull();
    }

    @Test
    void projectTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        employee.addProject(projectBack);
        assertThat(employee.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getEmployee()).isEqualTo(employee);

        employee.removeProject(projectBack);
        assertThat(employee.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getEmployee()).isNull();

        employee.projects(new HashSet<>(Set.of(projectBack)));
        assertThat(employee.getProjects()).containsOnly(projectBack);
        assertThat(projectBack.getEmployee()).isEqualTo(employee);

        employee.setProjects(new HashSet<>());
        assertThat(employee.getProjects()).doesNotContain(projectBack);
        assertThat(projectBack.getEmployee()).isNull();
    }

    @Test
    void timesheetsTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        TimeSheet timeSheetBack = getTimeSheetRandomSampleGenerator();

        employee.addTimesheets(timeSheetBack);
        assertThat(employee.getTimesheets()).containsOnly(timeSheetBack);
        assertThat(timeSheetBack.getEmployee()).isEqualTo(employee);

        employee.removeTimesheets(timeSheetBack);
        assertThat(employee.getTimesheets()).doesNotContain(timeSheetBack);
        assertThat(timeSheetBack.getEmployee()).isNull();

        employee.timesheets(new HashSet<>(Set.of(timeSheetBack)));
        assertThat(employee.getTimesheets()).containsOnly(timeSheetBack);
        assertThat(timeSheetBack.getEmployee()).isEqualTo(employee);

        employee.setTimesheets(new HashSet<>());
        assertThat(employee.getTimesheets()).doesNotContain(timeSheetBack);
        assertThat(timeSheetBack.getEmployee()).isNull();
    }

    @Test
    void expenseNotesTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        ExpenseNote expenseNoteBack = getExpenseNoteRandomSampleGenerator();

        employee.addExpenseNotes(expenseNoteBack);
        assertThat(employee.getExpenseNotes()).containsOnly(expenseNoteBack);
        assertThat(expenseNoteBack.getEmployee()).isEqualTo(employee);

        employee.removeExpenseNotes(expenseNoteBack);
        assertThat(employee.getExpenseNotes()).doesNotContain(expenseNoteBack);
        assertThat(expenseNoteBack.getEmployee()).isNull();

        employee.expenseNotes(new HashSet<>(Set.of(expenseNoteBack)));
        assertThat(employee.getExpenseNotes()).containsOnly(expenseNoteBack);
        assertThat(expenseNoteBack.getEmployee()).isEqualTo(employee);

        employee.setExpenseNotes(new HashSet<>());
        assertThat(employee.getExpenseNotes()).doesNotContain(expenseNoteBack);
        assertThat(expenseNoteBack.getEmployee()).isNull();
    }

    @Test
    void documentsTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        employee.addDocuments(documentBack);
        assertThat(employee.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getEmployee()).isEqualTo(employee);

        employee.removeDocuments(documentBack);
        assertThat(employee.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getEmployee()).isNull();

        employee.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(employee.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getEmployee()).isEqualTo(employee);

        employee.setDocuments(new HashSet<>());
        assertThat(employee.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getEmployee()).isNull();
    }

    @Test
    void activityReportsTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        ActivityReport activityReportBack = getActivityReportRandomSampleGenerator();

        employee.addActivityReports(activityReportBack);
        assertThat(employee.getActivityReports()).containsOnly(activityReportBack);
        assertThat(activityReportBack.getEmployee()).isEqualTo(employee);

        employee.removeActivityReports(activityReportBack);
        assertThat(employee.getActivityReports()).doesNotContain(activityReportBack);
        assertThat(activityReportBack.getEmployee()).isNull();

        employee.activityReports(new HashSet<>(Set.of(activityReportBack)));
        assertThat(employee.getActivityReports()).containsOnly(activityReportBack);
        assertThat(activityReportBack.getEmployee()).isEqualTo(employee);

        employee.setActivityReports(new HashSet<>());
        assertThat(employee.getActivityReports()).doesNotContain(activityReportBack);
        assertThat(activityReportBack.getEmployee()).isNull();
    }

    @Test
    void paySlipsTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        PaySlip paySlipBack = getPaySlipRandomSampleGenerator();

        employee.addPaySlips(paySlipBack);
        assertThat(employee.getPaySlips()).containsOnly(paySlipBack);
        assertThat(paySlipBack.getEmployee()).isEqualTo(employee);

        employee.removePaySlips(paySlipBack);
        assertThat(employee.getPaySlips()).doesNotContain(paySlipBack);
        assertThat(paySlipBack.getEmployee()).isNull();

        employee.paySlips(new HashSet<>(Set.of(paySlipBack)));
        assertThat(employee.getPaySlips()).containsOnly(paySlipBack);
        assertThat(paySlipBack.getEmployee()).isEqualTo(employee);

        employee.setPaySlips(new HashSet<>());
        assertThat(employee.getPaySlips()).doesNotContain(paySlipBack);
        assertThat(paySlipBack.getEmployee()).isNull();
    }

    @Test
    void managerTest() throws Exception {
        Employee employee = getEmployeeRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        employee.setManager(employeeBack);
        assertThat(employee.getManager()).isEqualTo(employeeBack);

        employee.manager(null);
        assertThat(employee.getManager()).isNull();
    }
}
