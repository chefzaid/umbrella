package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.AppUserTestSamples.*;
import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.EnterpriseTestSamples.*;
import static io.simpleit.umbrella.domain.ExpenseTestSamples.*;
import static io.simpleit.umbrella.domain.ParameterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parameter.class);
        Parameter parameter1 = getParameterSample1();
        Parameter parameter2 = new Parameter();
        assertThat(parameter1).isNotEqualTo(parameter2);

        parameter2.setId(parameter1.getId());
        assertThat(parameter1).isEqualTo(parameter2);

        parameter2 = getParameterSample2();
        assertThat(parameter1).isNotEqualTo(parameter2);
    }

    @Test
    void appUserTest() throws Exception {
        Parameter parameter = getParameterRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        parameter.setAppUser(appUserBack);
        assertThat(parameter.getAppUser()).isEqualTo(appUserBack);

        parameter.appUser(null);
        assertThat(parameter.getAppUser()).isNull();
    }

    @Test
    void documentTest() throws Exception {
        Parameter parameter = getParameterRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        parameter.setDocument(documentBack);
        assertThat(parameter.getDocument()).isEqualTo(documentBack);
        assertThat(documentBack.getDocumentType()).isEqualTo(parameter);

        parameter.document(null);
        assertThat(parameter.getDocument()).isNull();
        assertThat(documentBack.getDocumentType()).isNull();
    }

    @Test
    void expenseTest() throws Exception {
        Parameter parameter = getParameterRandomSampleGenerator();
        Expense expenseBack = getExpenseRandomSampleGenerator();

        parameter.setExpense(expenseBack);
        assertThat(parameter.getExpense()).isEqualTo(expenseBack);
        assertThat(expenseBack.getPaymentMethod()).isEqualTo(parameter);

        parameter.expense(null);
        assertThat(parameter.getExpense()).isNull();
        assertThat(expenseBack.getPaymentMethod()).isNull();
    }

    @Test
    void enterpriseTest() throws Exception {
        Parameter parameter = getParameterRandomSampleGenerator();
        Enterprise enterpriseBack = getEnterpriseRandomSampleGenerator();

        parameter.setEnterprise(enterpriseBack);
        assertThat(parameter.getEnterprise()).isEqualTo(enterpriseBack);

        parameter.enterprise(null);
        assertThat(parameter.getEnterprise()).isNull();
    }
}
