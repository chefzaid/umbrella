package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.NotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void authorTest() throws Exception {
        Notification notification = getNotificationRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        notification.setAuthor(employeeBack);
        assertThat(notification.getAuthor()).isEqualTo(employeeBack);

        notification.author(null);
        assertThat(notification.getAuthor()).isNull();
    }

    @Test
    void receiverTest() throws Exception {
        Notification notification = getNotificationRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        notification.setReceiver(employeeBack);
        assertThat(notification.getReceiver()).isEqualTo(employeeBack);

        notification.receiver(null);
        assertThat(notification.getReceiver()).isNull();
    }
}
