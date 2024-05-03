package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ActivityReportTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.OperationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ActivityReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityReport.class);
        ActivityReport activityReport1 = getActivityReportSample1();
        ActivityReport activityReport2 = new ActivityReport();
        assertThat(activityReport1).isNotEqualTo(activityReport2);

        activityReport2.setId(activityReport1.getId());
        assertThat(activityReport1).isEqualTo(activityReport2);

        activityReport2 = getActivityReportSample2();
        assertThat(activityReport1).isNotEqualTo(activityReport2);
    }

    @Test
    void operationsTest() throws Exception {
        ActivityReport activityReport = getActivityReportRandomSampleGenerator();
        Operation operationBack = getOperationRandomSampleGenerator();

        activityReport.addOperations(operationBack);
        assertThat(activityReport.getOperations()).containsOnly(operationBack);
        assertThat(operationBack.getActivityReport()).isEqualTo(activityReport);

        activityReport.removeOperations(operationBack);
        assertThat(activityReport.getOperations()).doesNotContain(operationBack);
        assertThat(operationBack.getActivityReport()).isNull();

        activityReport.operations(new HashSet<>(Set.of(operationBack)));
        assertThat(activityReport.getOperations()).containsOnly(operationBack);
        assertThat(operationBack.getActivityReport()).isEqualTo(activityReport);

        activityReport.setOperations(new HashSet<>());
        assertThat(activityReport.getOperations()).doesNotContain(operationBack);
        assertThat(operationBack.getActivityReport()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        ActivityReport activityReport = getActivityReportRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        activityReport.setEmployee(employeeBack);
        assertThat(activityReport.getEmployee()).isEqualTo(employeeBack);

        activityReport.employee(null);
        assertThat(activityReport.getEmployee()).isNull();
    }
}
