package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.EmploymentContractTestSamples.*;
import static io.simpleit.umbrella.domain.FormulaTestSamples.*;
import static io.simpleit.umbrella.domain.ProspectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormulaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formula.class);
        Formula formula1 = getFormulaSample1();
        Formula formula2 = new Formula();
        assertThat(formula1).isNotEqualTo(formula2);

        formula2.setId(formula1.getId());
        assertThat(formula1).isEqualTo(formula2);

        formula2 = getFormulaSample2();
        assertThat(formula1).isNotEqualTo(formula2);
    }

    @Test
    void prospectTest() throws Exception {
        Formula formula = getFormulaRandomSampleGenerator();
        Prospect prospectBack = getProspectRandomSampleGenerator();

        formula.setProspect(prospectBack);
        assertThat(formula.getProspect()).isEqualTo(prospectBack);
        assertThat(prospectBack.getFormula()).isEqualTo(formula);

        formula.prospect(null);
        assertThat(formula.getProspect()).isNull();
        assertThat(prospectBack.getFormula()).isNull();
    }

    @Test
    void employmentContractTest() throws Exception {
        Formula formula = getFormulaRandomSampleGenerator();
        EmploymentContract employmentContractBack = getEmploymentContractRandomSampleGenerator();

        formula.setEmploymentContract(employmentContractBack);
        assertThat(formula.getEmploymentContract()).isEqualTo(employmentContractBack);
        assertThat(employmentContractBack.getForumula()).isEqualTo(formula);

        formula.employmentContract(null);
        assertThat(formula.getEmploymentContract()).isNull();
        assertThat(employmentContractBack.getForumula()).isNull();
    }
}
