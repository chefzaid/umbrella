package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.ExpenseNoteTestSamples.*;
import static io.simpleit.umbrella.domain.ExpenseTestSamples.*;
import static io.simpleit.umbrella.domain.MileageAllowanceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExpenseNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseNote.class);
        ExpenseNote expenseNote1 = getExpenseNoteSample1();
        ExpenseNote expenseNote2 = new ExpenseNote();
        assertThat(expenseNote1).isNotEqualTo(expenseNote2);

        expenseNote2.setId(expenseNote1.getId());
        assertThat(expenseNote1).isEqualTo(expenseNote2);

        expenseNote2 = getExpenseNoteSample2();
        assertThat(expenseNote1).isNotEqualTo(expenseNote2);
    }

    @Test
    void mileageAllowanceTest() throws Exception {
        ExpenseNote expenseNote = getExpenseNoteRandomSampleGenerator();
        MileageAllowance mileageAllowanceBack = getMileageAllowanceRandomSampleGenerator();

        expenseNote.setMileageAllowance(mileageAllowanceBack);
        assertThat(expenseNote.getMileageAllowance()).isEqualTo(mileageAllowanceBack);

        expenseNote.mileageAllowance(null);
        assertThat(expenseNote.getMileageAllowance()).isNull();
    }

    @Test
    void documentTest() throws Exception {
        ExpenseNote expenseNote = getExpenseNoteRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        expenseNote.setDocument(documentBack);
        assertThat(expenseNote.getDocument()).isEqualTo(documentBack);

        expenseNote.document(null);
        assertThat(expenseNote.getDocument()).isNull();
    }

    @Test
    void expensesTest() throws Exception {
        ExpenseNote expenseNote = getExpenseNoteRandomSampleGenerator();
        Expense expenseBack = getExpenseRandomSampleGenerator();

        expenseNote.addExpenses(expenseBack);
        assertThat(expenseNote.getExpenses()).containsOnly(expenseBack);
        assertThat(expenseBack.getExpenseNote()).isEqualTo(expenseNote);

        expenseNote.removeExpenses(expenseBack);
        assertThat(expenseNote.getExpenses()).doesNotContain(expenseBack);
        assertThat(expenseBack.getExpenseNote()).isNull();

        expenseNote.expenses(new HashSet<>(Set.of(expenseBack)));
        assertThat(expenseNote.getExpenses()).containsOnly(expenseBack);
        assertThat(expenseBack.getExpenseNote()).isEqualTo(expenseNote);

        expenseNote.setExpenses(new HashSet<>());
        assertThat(expenseNote.getExpenses()).doesNotContain(expenseBack);
        assertThat(expenseBack.getExpenseNote()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        ExpenseNote expenseNote = getExpenseNoteRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        expenseNote.setEmployee(employeeBack);
        assertThat(expenseNote.getEmployee()).isEqualTo(employeeBack);

        expenseNote.employee(null);
        assertThat(expenseNote.getEmployee()).isNull();
    }
}
