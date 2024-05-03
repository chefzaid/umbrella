package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.AddressTestSamples.*;
import static io.simpleit.umbrella.domain.ClientTestSamples.*;
import static io.simpleit.umbrella.domain.ContactTestSamples.*;
import static io.simpleit.umbrella.domain.ProjectTestSamples.*;
import static io.simpleit.umbrella.domain.ServiceContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void addressTest() throws Exception {
        Client client = getClientRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        client.setAddress(addressBack);
        assertThat(client.getAddress()).isEqualTo(addressBack);

        client.address(null);
        assertThat(client.getAddress()).isNull();
    }

    @Test
    void contactsTest() throws Exception {
        Client client = getClientRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        client.addContacts(contactBack);
        assertThat(client.getContacts()).containsOnly(contactBack);
        assertThat(contactBack.getClient()).isEqualTo(client);

        client.removeContacts(contactBack);
        assertThat(client.getContacts()).doesNotContain(contactBack);
        assertThat(contactBack.getClient()).isNull();

        client.contacts(new HashSet<>(Set.of(contactBack)));
        assertThat(client.getContacts()).containsOnly(contactBack);
        assertThat(contactBack.getClient()).isEqualTo(client);

        client.setContacts(new HashSet<>());
        assertThat(client.getContacts()).doesNotContain(contactBack);
        assertThat(contactBack.getClient()).isNull();
    }

    @Test
    void serviceContractTest() throws Exception {
        Client client = getClientRandomSampleGenerator();
        ServiceContract serviceContractBack = getServiceContractRandomSampleGenerator();

        client.setServiceContract(serviceContractBack);
        assertThat(client.getServiceContract()).isEqualTo(serviceContractBack);
        assertThat(serviceContractBack.getEmployee()).isEqualTo(client);

        client.serviceContract(null);
        assertThat(client.getServiceContract()).isNull();
        assertThat(serviceContractBack.getEmployee()).isNull();
    }

    @Test
    void projectTest() throws Exception {
        Client client = getClientRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        client.setProject(projectBack);
        assertThat(client.getProject()).isEqualTo(projectBack);
        assertThat(projectBack.getClient()).isEqualTo(client);

        client.project(null);
        assertThat(client.getProject()).isNull();
        assertThat(projectBack.getClient()).isNull();
    }
}
