package io.simpleit.umbrella.domain;

import static io.simpleit.umbrella.domain.DocumentTestSamples.*;
import static io.simpleit.umbrella.domain.InvoiceLineTestSamples.*;
import static io.simpleit.umbrella.domain.InvoiceTestSamples.*;
import static io.simpleit.umbrella.domain.ProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.simpleit.umbrella.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void pojectTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        invoice.setPoject(projectBack);
        assertThat(invoice.getPoject()).isEqualTo(projectBack);

        invoice.poject(null);
        assertThat(invoice.getPoject()).isNull();
    }

    @Test
    void documentTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        invoice.setDocument(documentBack);
        assertThat(invoice.getDocument()).isEqualTo(documentBack);

        invoice.document(null);
        assertThat(invoice.getDocument()).isNull();
    }

    @Test
    void linesTest() throws Exception {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        InvoiceLine invoiceLineBack = getInvoiceLineRandomSampleGenerator();

        invoice.addLines(invoiceLineBack);
        assertThat(invoice.getLines()).containsOnly(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isEqualTo(invoice);

        invoice.removeLines(invoiceLineBack);
        assertThat(invoice.getLines()).doesNotContain(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isNull();

        invoice.lines(new HashSet<>(Set.of(invoiceLineBack)));
        assertThat(invoice.getLines()).containsOnly(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isEqualTo(invoice);

        invoice.setLines(new HashSet<>());
        assertThat(invoice.getLines()).doesNotContain(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isNull();
    }
}
