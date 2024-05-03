package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ExpenseTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseType.class);
        ExpenseType expenseType1 = getExpenseTypeSample1();
        ExpenseType expenseType2 = new ExpenseType();
        assertThat(expenseType1).isNotEqualTo(expenseType2);

        expenseType2.setId(expenseType1.getId());
        assertThat(expenseType1).isEqualTo(expenseType2);

        expenseType2 = getExpenseTypeSample2();
        assertThat(expenseType1).isNotEqualTo(expenseType2);
    }
}
