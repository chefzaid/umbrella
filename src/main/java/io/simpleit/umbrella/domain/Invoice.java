package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "label")
    private String label;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private Instant issueDate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "sales_tax_pct")
    private Double salesTaxPct;

    @Column(name = "terms")
    private String terms;

    @JsonIgnoreProperties(value = { "client", "employee", "timeSheet", "expense", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Project poject;

    @JsonIgnoreProperties(
        value = {
            "documentType",
            "employee",
            "idDocument",
            "employmentContract",
            "serviceContract",
            "timeSheet",
            "expenseNote",
            "invoice",
            "paySlip",
            "enterprise",
        },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Document document;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoice" }, allowSetters = true)
    private Set<InvoiceLine> lines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public Invoice invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getLabel() {
        return this.label;
    }

    public Invoice label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getIssueDate() {
        return this.issueDate;
    }

    public Invoice issueDate(Instant issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Invoice currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getSalesTaxPct() {
        return this.salesTaxPct;
    }

    public Invoice salesTaxPct(Double salesTaxPct) {
        this.setSalesTaxPct(salesTaxPct);
        return this;
    }

    public void setSalesTaxPct(Double salesTaxPct) {
        this.salesTaxPct = salesTaxPct;
    }

    public String getTerms() {
        return this.terms;
    }

    public Invoice terms(String terms) {
        this.setTerms(terms);
        return this;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public Project getPoject() {
        return this.poject;
    }

    public void setPoject(Project project) {
        this.poject = project;
    }

    public Invoice poject(Project project) {
        this.setPoject(project);
        return this;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Invoice document(Document document) {
        this.setDocument(document);
        return this;
    }

    public Set<InvoiceLine> getLines() {
        return this.lines;
    }

    public void setLines(Set<InvoiceLine> invoiceLines) {
        if (this.lines != null) {
            this.lines.forEach(i -> i.setInvoice(null));
        }
        if (invoiceLines != null) {
            invoiceLines.forEach(i -> i.setInvoice(this));
        }
        this.lines = invoiceLines;
    }

    public Invoice lines(Set<InvoiceLine> invoiceLines) {
        this.setLines(invoiceLines);
        return this;
    }

    public Invoice addLines(InvoiceLine invoiceLine) {
        this.lines.add(invoiceLine);
        invoiceLine.setInvoice(this);
        return this;
    }

    public Invoice removeLines(InvoiceLine invoiceLine) {
        this.lines.remove(invoiceLine);
        invoiceLine.setInvoice(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", label='" + getLabel() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", salesTaxPct=" + getSalesTaxPct() +
            ", terms='" + getTerms() + "'" +
            "}";
    }
}
