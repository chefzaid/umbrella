package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ActivityReportTestSamples.*;
import static io.simpleit.umbrella.domain.OperationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operation.class);
        Operation operation1 = getOperationSample1();
        Operation operation2 = new Operation();
        assertThat(operation1).isNotEqualTo(operation2);

        operation2.setId(operation1.getId());
        assertThat(operation1).isEqualTo(operation2);

        operation2 = getOperationSample2();
        assertThat(operation1).isNotEqualTo(operation2);
    }

    @Test
    void activityReportTest() throws Exception {
        Operation operation = getOperationRandomSampleGenerator();
        ActivityReport activityReportBack = getActivityReportRandomSampleGenerator();

        operation.setActivityReport(activityReportBack);
        assertThat(operation.getActivityReport()).isEqualTo(activityReportBack);

        operation.activityReport(null);
        assertThat(operation.getActivityReport()).isNull();
    }
}
