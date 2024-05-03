package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parameter.
 */
@Entity
@Table(name = "parameter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contact", "preferences", "employee" }, allowSetters = true)
    private AppUser appUser;

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
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "documentType")
    private Document document;

    @JsonIgnoreProperties(value = { "paymentMethod", "project", "expenseNote" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paymentMethod")
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parameters", "documents" }, allowSetters = true)
    private Enterprise enterprise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parameter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Parameter label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return this.value;
    }

    public Parameter value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Parameter appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        if (this.document != null) {
            this.document.setDocumentType(null);
        }
        if (document != null) {
            document.setDocumentType(this);
        }
        this.document = document;
    }

    public Parameter document(Document document) {
        this.setDocument(document);
        return this;
    }

    public Expense getExpense() {
        return this.expense;
    }

    public void setExpense(Expense expense) {
        if (this.expense != null) {
            this.expense.setPaymentMethod(null);
        }
        if (expense != null) {
            expense.setPaymentMethod(this);
        }
        this.expense = expense;
    }

    public Parameter expense(Expense expense) {
        this.setExpense(expense);
        return this;
    }

    public Enterprise getEnterprise() {
        return this.enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Parameter enterprise(Enterprise enterprise) {
        this.setEnterprise(enterprise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parameter)) {
            return false;
        }
        return getId() != null && getId().equals(((Parameter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parameter{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
