package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ExpenseNoteTestSamples.*;
import static io.simpleit.umbrella.domain.MileageAllowanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MileageAllowanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MileageAllowance.class);
        MileageAllowance mileageAllowance1 = getMileageAllowanceSample1();
        MileageAllowance mileageAllowance2 = new MileageAllowance();
        assertThat(mileageAllowance1).isNotEqualTo(mileageAllowance2);

        mileageAllowance2.setId(mileageAllowance1.getId());
        assertThat(mileageAllowance1).isEqualTo(mileageAllowance2);

        mileageAllowance2 = getMileageAllowanceSample2();
        assertThat(mileageAllowance1).isNotEqualTo(mileageAllowance2);
    }

    @Test
    void expenseNoteTest() throws Exception {
        MileageAllowance mileageAllowance = getMileageAllowanceRandomSampleGenerator();
        ExpenseNote expenseNoteBack = getExpenseNoteRandomSampleGenerator();

        mileageAllowance.setExpenseNote(expenseNoteBack);
        assertThat(mileageAllowance.getExpenseNote()).isEqualTo(expenseNoteBack);
        assertThat(expenseNoteBack.getMileageAllowance()).isEqualTo(mileageAllowance);

        mileageAllowance.expenseNote(null);
        assertThat(mileageAllowance.getExpenseNote()).isNull();
        assertThat(expenseNoteBack.getMileageAllowance()).isNull();
    }
}
