package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ExpenseNoteTestSamples.*;
import static io.simpleit.umbrella.domain.ExpenseTestSamples.*;
import static io.simpleit.umbrella.domain.ParameterTestSamples.*;
import static io.simpleit.umbrella.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Expense.class);
        Expense expense1 = getExpenseSample1();
        Expense expense2 = new Expense();
        assertThat(expense1).isNotEqualTo(expense2);

        expense2.setId(expense1.getId());
        assertThat(expense1).isEqualTo(expense2);

        expense2 = getExpenseSample2();
        assertThat(expense1).isNotEqualTo(expense2);
    }

    @Test
    void paymentMethodTest() throws Exception {
        Expense expense = getExpenseRandomSampleGenerator();
        Parameter parameterBack = getParameterRandomSampleGenerator();

        expense.setPaymentMethod(parameterBack);
        assertThat(expense.getPaymentMethod()).isEqualTo(parameterBack);

        expense.paymentMethod(null);
        assertThat(expense.getPaymentMethod()).isNull();
    }

    @Test
    void projectTest() throws Exception {
        Expense expense = getExpenseRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        expense.setProject(projectBack);
        assertThat(expense.getProject()).isEqualTo(projectBack);

        expense.project(null);
        assertThat(expense.getProject()).isNull();
    }

    @Test
    void expenseNoteTest() throws Exception {
        Expense expense = getExpenseRandomSampleGenerator();
        ExpenseNote expenseNoteBack = getExpenseNoteRandomSampleGenerator();

        expense.setExpenseNote(expenseNoteBack);
        assertThat(expense.getExpenseNote()).isEqualTo(expenseNoteBack);

        expense.expenseNote(null);
        assertThat(expense.getExpenseNote()).isNull();
    }
}
