package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MileageAllowance.
 */
@Entity
@Table(name = "mileage_allowance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MileageAllowance implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "mileage", nullable = false)
    private Double mileage;

    @Column(name = "multiplier")
    private Long multiplier;

    @JsonIgnoreProperties(value = { "mileageAllowance", "document", "expenses", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mileageAllowance")
    private ExpenseNote expenseNote;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MileageAllowance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMileage() {
        return this.mileage;
    }

    public MileageAllowance mileage(Double mileage) {
        this.setMileage(mileage);
        return this;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Long getMultiplier() {
        return this.multiplier;
    }

    public MileageAllowance multiplier(Long multiplier) {
        this.setMultiplier(multiplier);
        return this;
    }

    public void setMultiplier(Long multiplier) {
        this.multiplier = multiplier;
    }

    public ExpenseNote getExpenseNote() {
        return this.expenseNote;
    }

    public void setExpenseNote(ExpenseNote expenseNote) {
        if (this.expenseNote != null) {
            this.expenseNote.setMileageAllowance(null);
        }
        if (expenseNote != null) {
            expenseNote.setMileageAllowance(this);
        }
        this.expenseNote = expenseNote;
    }

    public MileageAllowance expenseNote(ExpenseNote expenseNote) {
        this.setExpenseNote(expenseNote);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MileageAllowance)) {
            return false;
        }
        return getId() != null && getId().equals(((MileageAllowance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MileageAllowance{" +
            "id=" + getId() +
            ", mileage=" + getMileage() +
            ", multiplier=" + getMultiplier() +
            "}";
    }
}
