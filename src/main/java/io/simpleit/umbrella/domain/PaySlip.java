package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaySlip.
 */
@Entity
@Table(name = "pay_slip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaySlip implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "super_gross_salary")
    private Double superGrossSalary;

    @Column(name = "gross_salary")
    private Double grossSalary;

    @Column(name = "net_salary")
    private Double netSalary;

    @Column(name = "tax_rate")
    private Double taxRate;

    @Column(name = "amount_paid")
    private Double amountPaid;

    @Column(name = "total_expenses")
    private Double totalExpenses;

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

    public PaySlip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSuperGrossSalary() {
        return this.superGrossSalary;
    }

    public PaySlip superGrossSalary(Double superGrossSalary) {
        this.setSuperGrossSalary(superGrossSalary);
        return this;
    }

    public void setSuperGrossSalary(Double superGrossSalary) {
        this.superGrossSalary = superGrossSalary;
    }

    public Double getGrossSalary() {
        return this.grossSalary;
    }

    public PaySlip grossSalary(Double grossSalary) {
        this.setGrossSalary(grossSalary);
        return this;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Double getNetSalary() {
        return this.netSalary;
    }

    public PaySlip netSalary(Double netSalary) {
        this.setNetSalary(netSalary);
        return this;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public Double getTaxRate() {
        return this.taxRate;
    }

    public PaySlip taxRate(Double taxRate) {
        this.setTaxRate(taxRate);
        return this;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getAmountPaid() {
        return this.amountPaid;
    }

    public PaySlip amountPaid(Double amountPaid) {
        this.setAmountPaid(amountPaid);
        return this;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Double getTotalExpenses() {
        return this.totalExpenses;
    }

    public PaySlip totalExpenses(Double totalExpenses) {
        this.setTotalExpenses(totalExpenses);
        return this;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public PaySlip document(Document document) {
        this.setDocument(document);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PaySlip employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaySlip)) {
            return false;
        }
        return getId() != null && getId().equals(((PaySlip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaySlip{" +
            "id=" + getId() +
            ", superGrossSalary=" + getSuperGrossSalary() +
            ", grossSalary=" + getGrossSalary() +
            ", netSalary=" + getNetSalary() +
            ", taxRate=" + getTaxRate() +
            ", amountPaid=" + getAmountPaid() +
            ", totalExpenses=" + getTotalExpenses() +
            "}";
    }
}
