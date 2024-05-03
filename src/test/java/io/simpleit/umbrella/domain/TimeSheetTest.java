package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.ProjectTestSamples.*;
import static io.simpleit.umbrella.domain.TimeSheetLineTestSamples.*;
import static io.simpleit.umbrella.domain.TimeSheetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TimeSheetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSheet.class);
        TimeSheet timeSheet1 = getTimeSheetSample1();
        TimeSheet timeSheet2 = new TimeSheet();
        assertThat(timeSheet1).isNotEqualTo(timeSheet2);

        timeSheet2.setId(timeSheet1.getId());
        assertThat(timeSheet1).isEqualTo(timeSheet2);

        timeSheet2 = getTimeSheetSample2();
        assertThat(timeSheet1).isNotEqualTo(timeSheet2);
    }

    @Test
    void projectTest() throws Exception {
        TimeSheet timeSheet = getTimeSheetRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        timeSheet.setProject(projectBack);
        assertThat(timeSheet.getProject()).isEqualTo(projectBack);

        timeSheet.project(null);
        assertThat(timeSheet.getProject()).isNull();
    }

    @Test
    void documentTest() throws Exception {
        TimeSheet timeSheet = getTimeSheetRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        timeSheet.setDocument(documentBack);
        assertThat(timeSheet.getDocument()).isEqualTo(documentBack);

        timeSheet.document(null);
        assertThat(timeSheet.getDocument()).isNull();
    }

    @Test
    void linesTest() throws Exception {
        TimeSheet timeSheet = getTimeSheetRandomSampleGenerator();
        TimeSheetLine timeSheetLineBack = getTimeSheetLineRandomSampleGenerator();

        timeSheet.addLines(timeSheetLineBack);
        assertThat(timeSheet.getLines()).containsOnly(timeSheetLineBack);
        assertThat(timeSheetLineBack.getTimeSheet()).isEqualTo(timeSheet);

        timeSheet.removeLines(timeSheetLineBack);
        assertThat(timeSheet.getLines()).doesNotContain(timeSheetLineBack);
        assertThat(timeSheetLineBack.getTimeSheet()).isNull();

        timeSheet.lines(new HashSet<>(Set.of(timeSheetLineBack)));
        assertThat(timeSheet.getLines()).containsOnly(timeSheetLineBack);
        assertThat(timeSheetLineBack.getTimeSheet()).isEqualTo(timeSheet);

        timeSheet.setLines(new HashSet<>());
        assertThat(timeSheet.getLines()).doesNotContain(timeSheetLineBack);
        assertThat(timeSheetLineBack.getTimeSheet()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        TimeSheet timeSheet = getTimeSheetRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        timeSheet.setEmployee(employeeBack);
        assertThat(timeSheet.getEmployee()).isEqualTo(employeeBack);

        timeSheet.employee(null);
        assertThat(timeSheet.getEmployee()).isNull();
    }
}
