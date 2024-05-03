package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ParameterGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParameterGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterGroup.class);
        ParameterGroup parameterGroup1 = getParameterGroupSample1();
        ParameterGroup parameterGroup2 = new ParameterGroup();
        assertThat(parameterGroup1).isNotEqualTo(parameterGroup2);

        parameterGroup2.setId(parameterGroup1.getId());
        assertThat(parameterGroup1).isEqualTo(parameterGroup2);

        parameterGroup2 = getParameterGroupSample2();
        assertThat(parameterGroup1).isNotEqualTo(parameterGroup2);
    }
}
