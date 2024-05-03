package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.FormulaTestSamples.*;
import static io.simpleit.umbrella.domain.ProspectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProspectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prospect.class);
        Prospect prospect1 = getProspectSample1();
        Prospect prospect2 = new Prospect();
        assertThat(prospect1).isNotEqualTo(prospect2);

        prospect2.setId(prospect1.getId());
        assertThat(prospect1).isEqualTo(prospect2);

        prospect2 = getProspectSample2();
        assertThat(prospect1).isNotEqualTo(prospect2);
    }

    @Test
    void formulaTest() throws Exception {
        Prospect prospect = getProspectRandomSampleGenerator();
        Formula formulaBack = getFormulaRandomSampleGenerator();

        prospect.setFormula(formulaBack);
        assertThat(prospect.getFormula()).isEqualTo(formulaBack);

        prospect.formula(null);
        assertThat(prospect.getFormula()).isNull();
    }
}
