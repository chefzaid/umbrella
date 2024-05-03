package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prospect.
 */
@Entity
@Table(name = "prospect")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prospect implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "daily_rate")
    private Long dailyRate;

    @Column(name = "monthly_expenses")
    private Long monthlyExpenses;

    @Column(name = "tax_rate")
    private Double taxRate;

    @Column(name = "expected_start_date")
    private LocalDate expectedStartDate;

    @Column(name = "expected_client")
    private String expectedClient;

    @Column(name = "notes")
    private String notes;

    @JsonIgnoreProperties(value = { "prospect", "employmentContract" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Formula formula;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prospect id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDailyRate() {
        return this.dailyRate;
    }

    public Prospect dailyRate(Long dailyRate) {
        this.setDailyRate(dailyRate);
        return this;
    }

    public void setDailyRate(Long dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Long getMonthlyExpenses() {
        return this.monthlyExpenses;
    }

    public Prospect monthlyExpenses(Long monthlyExpenses) {
        this.setMonthlyExpenses(monthlyExpenses);
        return this;
    }

    public void setMonthlyExpenses(Long monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public Double getTaxRate() {
        return this.taxRate;
    }

    public Prospect taxRate(Double taxRate) {
        this.setTaxRate(taxRate);
        return this;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public LocalDate getExpectedStartDate() {
        return this.expectedStartDate;
    }

    public Prospect expectedStartDate(LocalDate expectedStartDate) {
        this.setExpectedStartDate(expectedStartDate);
        return this;
    }

    public void setExpectedStartDate(LocalDate expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
    }

    public String getExpectedClient() {
        return this.expectedClient;
    }

    public Prospect expectedClient(String expectedClient) {
        this.setExpectedClient(expectedClient);
        return this;
    }

    public void setExpectedClient(String expectedClient) {
        this.expectedClient = expectedClient;
    }

    public String getNotes() {
        return this.notes;
    }

    public Prospect notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Formula getFormula() {
        return this.formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public Prospect formula(Formula formula) {
        this.setFormula(formula);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prospect)) {
            return false;
        }
        return getId() != null && getId().equals(((Prospect) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prospect{" +
            "id=" + getId() +
            ", dailyRate=" + getDailyRate() +
            ", monthlyExpenses=" + getMonthlyExpenses() +
            ", taxRate=" + getTaxRate() +
            ", expectedStartDate='" + getExpectedStartDate() + "'" +
            ", expectedClient='" + getExpectedClient() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
