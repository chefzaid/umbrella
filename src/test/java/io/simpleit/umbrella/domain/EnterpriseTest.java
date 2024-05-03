package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.EnterpriseTestSamples.*;
import static io.simpleit.umbrella.domain.ParameterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EnterpriseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enterprise.class);
        Enterprise enterprise1 = getEnterpriseSample1();
        Enterprise enterprise2 = new Enterprise();
        assertThat(enterprise1).isNotEqualTo(enterprise2);

        enterprise2.setId(enterprise1.getId());
        assertThat(enterprise1).isEqualTo(enterprise2);

        enterprise2 = getEnterpriseSample2();
        assertThat(enterprise1).isNotEqualTo(enterprise2);
    }

    @Test
    void parametersTest() throws Exception {
        Enterprise enterprise = getEnterpriseRandomSampleGenerator();
        Parameter parameterBack = getParameterRandomSampleGenerator();

        enterprise.addParameters(parameterBack);
        assertThat(enterprise.getParameters()).containsOnly(parameterBack);
        assertThat(parameterBack.getEnterprise()).isEqualTo(enterprise);

        enterprise.removeParameters(parameterBack);
        assertThat(enterprise.getParameters()).doesNotContain(parameterBack);
        assertThat(parameterBack.getEnterprise()).isNull();

        enterprise.parameters(new HashSet<>(Set.of(parameterBack)));
        assertThat(enterprise.getParameters()).containsOnly(parameterBack);
        assertThat(parameterBack.getEnterprise()).isEqualTo(enterprise);

        enterprise.setParameters(new HashSet<>());
        assertThat(enterprise.getParameters()).doesNotContain(parameterBack);
        assertThat(parameterBack.getEnterprise()).isNull();
    }

    @Test
    void documentsTest() throws Exception {
        Enterprise enterprise = getEnterpriseRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        enterprise.addDocuments(documentBack);
        assertThat(enterprise.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getEnterprise()).isEqualTo(enterprise);

        enterprise.removeDocuments(documentBack);
        assertThat(enterprise.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getEnterprise()).isNull();

        enterprise.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(enterprise.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getEnterprise()).isEqualTo(enterprise);

        enterprise.setDocuments(new HashSet<>());
        assertThat(enterprise.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getEnterprise()).isNull();
    }
}
