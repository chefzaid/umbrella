package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.EmployeeTestSamples.*;
import static io.simpleit.umbrella.domain.WalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wallet.class);
        Wallet wallet1 = getWalletSample1();
        Wallet wallet2 = new Wallet();
        assertThat(wallet1).isNotEqualTo(wallet2);

        wallet2.setId(wallet1.getId());
        assertThat(wallet1).isEqualTo(wallet2);

        wallet2 = getWalletSample2();
        assertThat(wallet1).isNotEqualTo(wallet2);
    }

    @Test
    void employeeTest() throws Exception {
        Wallet wallet = getWalletRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        wallet.setEmployee(employeeBack);
        assertThat(wallet.getEmployee()).isEqualTo(employeeBack);
        assertThat(employeeBack.getWallet()).isEqualTo(wallet);

        wallet.employee(null);
        assertThat(wallet.getEmployee()).isNull();
        assertThat(employeeBack.getWallet()).isNull();
    }
}
