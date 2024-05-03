package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.AppUserTestSamples.*;
import static io.simpleit.umbrella.domain.ContactTestSamples.*;
import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.ParameterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void contactTest() throws Exception {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Contact contactBack = getContactRandomSampleGenerator();

        appUser.setContact(contactBack);
        assertThat(appUser.getContact()).isEqualTo(contactBack);

        appUser.contact(null);
        assertThat(appUser.getContact()).isNull();
    }

    @Test
    void preferencesTest() throws Exception {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Parameter parameterBack = getParameterRandomSampleGenerator();

        appUser.addPreferences(parameterBack);
        assertThat(appUser.getPreferences()).containsOnly(parameterBack);
        assertThat(parameterBack.getAppUser()).isEqualTo(appUser);

        appUser.removePreferences(parameterBack);
        assertThat(appUser.getPreferences()).doesNotContain(parameterBack);
        assertThat(parameterBack.getAppUser()).isNull();

        appUser.preferences(new HashSet<>(Set.of(parameterBack)));
        assertThat(appUser.getPreferences()).containsOnly(parameterBack);
        assertThat(parameterBack.getAppUser()).isEqualTo(appUser);

        appUser.setPreferences(new HashSet<>());
        assertThat(appUser.getPreferences()).doesNotContain(parameterBack);
        assertThat(parameterBack.getAppUser()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        appUser.setEmployee(employeeBack);
        assertThat(appUser.getEmployee()).isEqualTo(employeeBack);
        assertThat(employeeBack.getUser()).isEqualTo(appUser);

        appUser.employee(null);
        assertThat(appUser.getEmployee()).isNull();
        assertThat(employeeBack.getUser()).isNull();
    }
}
