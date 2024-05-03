package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.TimeSheetLineTestSamples.*;
import static io.simpleit.umbrella.domain.TimeSheetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeSheetLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeSheetLine.class);
        TimeSheetLine timeSheetLine1 = getTimeSheetLineSample1();
        TimeSheetLine timeSheetLine2 = new TimeSheetLine();
        assertThat(timeSheetLine1).isNotEqualTo(timeSheetLine2);

        timeSheetLine2.setId(timeSheetLine1.getId());
        assertThat(timeSheetLine1).isEqualTo(timeSheetLine2);

        timeSheetLine2 = getTimeSheetLineSample2();
        assertThat(timeSheetLine1).isNotEqualTo(timeSheetLine2);
    }

    @Test
    void timeSheetTest() throws Exception {
        TimeSheetLine timeSheetLine = getTimeSheetLineRandomSampleGenerator();
        TimeSheet timeSheetBack = getTimeSheetRandomSampleGenerator();

        timeSheetLine.setTimeSheet(timeSheetBack);
        assertThat(timeSheetLine.getTimeSheet()).isEqualTo(timeSheetBack);

        timeSheetLine.timeSheet(null);
        assertThat(timeSheetLine.getTimeSheet()).isNull();
    }
}
