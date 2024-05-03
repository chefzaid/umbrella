package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Expense.
 */
@Entity
@Table(name = "expense")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "tax")
    private Double tax;

    @Column(name = "expense_date")
    private LocalDate expenseDate;

    @Column(name = "rebillable_to_client")
    private Boolean rebillableToClient;

    @Column(name = "comment")
    private String comment;

    @Column(name = "submit_date")
    private Instant submitDate;

    @Column(name = "validation_date")
    private Instant validationDate;

    @JsonIgnoreProperties(value = { "appUser", "document", "expense", "enterprise" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Parameter paymentMethod;

    @JsonIgnoreProperties(value = { "client", "employee", "timeSheet", "expense", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "mileageAllowance", "document", "expenses", "employee" }, allowSetters = true)
    private ExpenseNote expenseNote;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Expense id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Expense label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Expense description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Expense amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Expense currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getTax() {
        return this.tax;
    }

    public Expense tax(Double tax) {
        this.setTax(tax);
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public LocalDate getExpenseDate() {
        return this.expenseDate;
    }

    public Expense expenseDate(LocalDate expenseDate) {
        this.setExpenseDate(expenseDate);
        return this;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Boolean getRebillableToClient() {
        return this.rebillableToClient;
    }

    public Expense rebillableToClient(Boolean rebillableToClient) {
        this.setRebillableToClient(rebillableToClient);
        return this;
    }

    public void setRebillableToClient(Boolean rebillableToClient) {
        this.rebillableToClient = rebillableToClient;
    }

    public String getComment() {
        return this.comment;
    }

    public Expense comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getSubmitDate() {
        return this.submitDate;
    }

    public Expense submitDate(Instant submitDate) {
        this.setSubmitDate(submitDate);
        return this;
    }

    public void setSubmitDate(Instant submitDate) {
        this.submitDate = submitDate;
    }

    public Instant getValidationDate() {
        return this.validationDate;
    }

    public Expense validationDate(Instant validationDate) {
        this.setValidationDate(validationDate);
        return this;
    }

    public void setValidationDate(Instant validationDate) {
        this.validationDate = validationDate;
    }

    public Parameter getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(Parameter parameter) {
        this.paymentMethod = parameter;
    }

    public Expense paymentMethod(Parameter parameter) {
        this.setPaymentMethod(parameter);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Expense project(Project project) {
        this.setProject(project);
        return this;
    }

    public ExpenseNote getExpenseNote() {
        return this.expenseNote;
    }

    public void setExpenseNote(ExpenseNote expenseNote) {
        this.expenseNote = expenseNote;
    }

    public Expense expenseNote(ExpenseNote expenseNote) {
        this.setExpenseNote(expenseNote);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expense)) {
            return false;
        }
        return getId() != null && getId().equals(((Expense) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", tax=" + getTax() +
            ", expenseDate='" + getExpenseDate() + "'" +
            ", rebillableToClient='" + getRebillableToClient() + "'" +
            ", comment='" + getComment() + "'" +
            ", submitDate='" + getSubmitDate() + "'" +
            ", validationDate='" + getValidationDate() + "'" +
            "}";
    }
}
