package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.AddressTestSamples.*;
import static io.simpleit.umbrella.domain.ClientTestSamples.*;
import static io.simpleit.umbrella.domain.ContactTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = getAddressSample1();
        Address address2 = new Address();
        assertThat(address1).isNotEqualTo(address2);

        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);

        address2 = getAddressSample2();
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    void contactTest() throws Exception {
        Address address = getAddressRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        address.setContact(contactBack);
        assertThat(address.getContact()).isEqualTo(contactBack);
        assertThat(contactBack.getAddress()).isEqualTo(address);

        address.contact(null);
        assertThat(address.getContact()).isNull();
        assertThat(contactBack.getAddress()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        Address address = getAddressRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        address.setEmployee(employeeBack);
        assertThat(address.getEmployee()).isEqualTo(employeeBack);
        assertThat(employeeBack.getAddress()).isEqualTo(address);

        address.employee(null);
        assertThat(address.getEmployee()).isNull();
        assertThat(employeeBack.getAddress()).isNull();
    }

    @Test
    void clientTest() throws Exception {
        Address address = getAddressRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        address.setClient(clientBack);
        assertThat(address.getClient()).isEqualTo(clientBack);
        assertThat(clientBack.getAddress()).isEqualTo(address);

        address.client(null);
        assertThat(address.getClient()).isNull();
        assertThat(clientBack.getAddress()).isNull();
    }
}
