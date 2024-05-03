package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.AddressTestSamples.*;
import static io.simpleit.umbrella.domain.AppUserTestSamples.*;
import static io.simpleit.umbrella.domain.ClientTestSamples.*;
import static io.simpleit.umbrella.domain.ContactTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contact.class);
        Contact contact1 = getContactSample1();
        Contact contact2 = new Contact();
        assertThat(contact1).isNotEqualTo(contact2);

        contact2.setId(contact1.getId());
        assertThat(contact1).isEqualTo(contact2);

        contact2 = getContactSample2();
        assertThat(contact1).isNotEqualTo(contact2);
    }

    @Test
    void addressTest() throws Exception {
        Contact contact = getContactRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        contact.setAddress(addressBack);
        assertThat(contact.getAddress()).isEqualTo(addressBack);

        contact.address(null);
        assertThat(contact.getAddress()).isNull();
    }

    @Test
    void appUserTest() throws Exception {
        Contact contact = getContactRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        contact.setAppUser(appUserBack);
        assertThat(contact.getAppUser()).isEqualTo(appUserBack);
        assertThat(appUserBack.getContact()).isEqualTo(contact);

        contact.appUser(null);
        assertThat(contact.getAppUser()).isNull();
        assertThat(appUserBack.getContact()).isNull();
    }

    @Test
    void clientTest() throws Exception {
        Contact contact = getContactRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        contact.setClient(clientBack);
        assertThat(contact.getClient()).isEqualTo(clientBack);

        contact.client(null);
        assertThat(contact.getClient()).isNull();
    }
}
