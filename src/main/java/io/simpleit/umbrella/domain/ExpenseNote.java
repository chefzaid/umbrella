package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExpenseNote.
 */
@Entity
@Table(name = "expense_note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "concerned_month")
    private String concernedMonth;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "submit_date")
    private Instant submitDate;

    @Column(name = "validation_date")
    private Instant validationDate;

    @JsonIgnoreProperties(value = { "expenseNote" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MileageAllowance mileageAllowance;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "expenseNote")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paymentMethod", "project", "expenseNote" }, allowSetters = true)
    private Set<Expense> expenses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "user",
            "address",
            "contract",
            "idDocument",
            "wallet",
            "projects",
            "timesheets",
            "expenseNotes",
            "documents",
            "activityReports",
            "paySlips",
            "manager",
        },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExpenseNote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public ExpenseNote label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getConcernedMonth() {
        return this.concernedMonth;
    }

    public ExpenseNote concernedMonth(String concernedMonth) {
        this.setConcernedMonth(concernedMonth);
        return this;
    }

    public void setConcernedMonth(String concernedMonth) {
        this.concernedMonth = concernedMonth;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public ExpenseNote creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getSubmitDate() {
        return this.submitDate;
    }

    public ExpenseNote submitDate(Instant submitDate) {
        this.setSubmitDate(submitDate);
        return this;
    }

    public void setSubmitDate(Instant submitDate) {
        this.submitDate = submitDate;
    }

    public Instant getValidationDate() {
        return this.validationDate;
    }

    public ExpenseNote validationDate(Instant validationDate) {
        this.setValidationDate(validationDate);
        return this;
    }

    public void setValidationDate(Instant validationDate) {
        this.validationDate = validationDate;
    }

    public MileageAllowance getMileageAllowance() {
        return this.mileageAllowance;
    }

    public void setMileageAllowance(MileageAllowance mileageAllowance) {
        this.mileageAllowance = mileageAllowance;
    }

    public ExpenseNote mileageAllowance(MileageAllowance mileageAllowance) {
        this.setMileageAllowance(mileageAllowance);
        return this;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public ExpenseNote document(Document document) {
        this.setDocument(document);
        return this;
    }

    public Set<Expense> getExpenses() {
        return this.expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        if (this.expenses != null) {
            this.expenses.forEach(i -> i.setExpenseNote(null));
        }
        if (expenses != null) {
            expenses.forEach(i -> i.setExpenseNote(this));
        }
        this.expenses = expenses;
    }

    public ExpenseNote expenses(Set<Expense> expenses) {
        this.setExpenses(expenses);
        return this;
    }

    public ExpenseNote addExpenses(Expense expense) {
        this.expenses.add(expense);
        expense.setExpenseNote(this);
        return this;
    }

    public ExpenseNote removeExpenses(Expense expense) {
        this.expenses.remove(expense);
        expense.setExpenseNote(null);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ExpenseNote employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpenseNote)) {
            return false;
        }
        return getId() != null && getId().equals(((ExpenseNote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseNote{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", concernedMonth='" + getConcernedMonth() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", submitDate='" + getSubmitDate() + "'" +
            ", validationDate='" + getValidationDate() + "'" +
            "}";
    }
}
