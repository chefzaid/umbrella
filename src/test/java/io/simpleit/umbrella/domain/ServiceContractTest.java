package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.ClientTestSamples.*;
import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.ServiceContractTestSamples.*;
import static io.simpleit.umbrella.domain.ServiceContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ServiceContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceContract.class);
        ServiceContract serviceContract1 = getServiceContractSample1();
        ServiceContract serviceContract2 = new ServiceContract();
        assertThat(serviceContract1).isNotEqualTo(serviceContract2);

        serviceContract2.setId(serviceContract1.getId());
        assertThat(serviceContract1).isEqualTo(serviceContract2);

        serviceContract2 = getServiceContractSample2();
        assertThat(serviceContract1).isNotEqualTo(serviceContract2);
    }

    @Test
    void employeeTest() throws Exception {
        ServiceContract serviceContract = getServiceContractRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        serviceContract.setEmployee(clientBack);
        assertThat(serviceContract.getEmployee()).isEqualTo(clientBack);

        serviceContract.employee(null);
        assertThat(serviceContract.getEmployee()).isNull();
    }

    @Test
    void documentsTest() throws Exception {
        ServiceContract serviceContract = getServiceContractRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        serviceContract.addDocuments(documentBack);
        assertThat(serviceContract.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getServiceContract()).isEqualTo(serviceContract);

        serviceContract.removeDocuments(documentBack);
        assertThat(serviceContract.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getServiceContract()).isNull();

        serviceContract.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(serviceContract.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getServiceContract()).isEqualTo(serviceContract);

        serviceContract.setDocuments(new HashSet<>());
        assertThat(serviceContract.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getServiceContract()).isNull();
    }

    @Test
    void amendmentsTest() throws Exception {
        ServiceContract serviceContract = getServiceContractRandomSampleGenerator();
        ServiceContract serviceContractBack = getServiceContractRandomSampleGenerator();

        serviceContract.addAmendments(serviceContractBack);
        assertThat(serviceContract.getAmendments()).containsOnly(serviceContractBack);
        assertThat(serviceContractBack.getServiceContract()).isEqualTo(serviceContract);

        serviceContract.removeAmendments(serviceContractBack);
        assertThat(serviceContract.getAmendments()).doesNotContain(serviceContractBack);
        assertThat(serviceContractBack.getServiceContract()).isNull();

        serviceContract.amendments(new HashSet<>(Set.of(serviceContractBack)));
        assertThat(serviceContract.getAmendments()).containsOnly(serviceContractBack);
        assertThat(serviceContractBack.getServiceContract()).isEqualTo(serviceContract);

        serviceContract.setAmendments(new HashSet<>());
        assertThat(serviceContract.getAmendments()).doesNotContain(serviceContractBack);
        assertThat(serviceContractBack.getServiceContract()).isNull();
    }

    @Test
    void serviceContractTest() throws Exception {
        ServiceContract serviceContract = getServiceContractRandomSampleGenerator();
        ServiceContract serviceContractBack = getServiceContractRandomSampleGenerator();

        serviceContract.setServiceContract(serviceContractBack);
        assertThat(serviceContract.getServiceContract()).isEqualTo(serviceContractBack);

        serviceContract.serviceContract(null);
        assertThat(serviceContract.getServiceContract()).isNull();
    }
}
