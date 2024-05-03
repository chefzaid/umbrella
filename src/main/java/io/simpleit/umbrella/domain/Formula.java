package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Formula.
 */
@Entity
@Table(name = "formula")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Formula implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "admin_fees_pct")
    private Double adminFeesPct;

    @Column(name = "additional_fees_pct")
    private Double additionalFeesPct;

    @JsonIgnoreProperties(value = { "formula" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "formula")
    private Prospect prospect;

    @JsonIgnoreProperties(value = { "forumula", "documents", "amendments", "employee", "employmentContract" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "forumula")
    private EmploymentContract employmentContract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Formula id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Formula label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getAdminFeesPct() {
        return this.adminFeesPct;
    }

    public Formula adminFeesPct(Double adminFeesPct) {
        this.setAdminFeesPct(adminFeesPct);
        return this;
    }

    public void setAdminFeesPct(Double adminFeesPct) {
        this.adminFeesPct = adminFeesPct;
    }

    public Double getAdditionalFeesPct() {
        return this.additionalFeesPct;
    }

    public Formula additionalFeesPct(Double additionalFeesPct) {
        this.setAdditionalFeesPct(additionalFeesPct);
        return this;
    }

    public void setAdditionalFeesPct(Double additionalFeesPct) {
        this.additionalFeesPct = additionalFeesPct;
    }

    public Prospect getProspect() {
        return this.prospect;
    }

    public void setProspect(Prospect prospect) {
        if (this.prospect != null) {
            this.prospect.setFormula(null);
        }
        if (prospect != null) {
            prospect.setFormula(this);
        }
        this.prospect = prospect;
    }

    public Formula prospect(Prospect prospect) {
        this.setProspect(prospect);
        return this;
    }

    public EmploymentContract getEmploymentContract() {
        return this.employmentContract;
    }

    public void setEmploymentContract(EmploymentContract employmentContract) {
        if (this.employmentContract != null) {
            this.employmentContract.setForumula(null);
        }
        if (employmentContract != null) {
            employmentContract.setForumula(this);
        }
        this.employmentContract = employmentContract;
    }

    public Formula employmentContract(EmploymentContract employmentContract) {
        this.setEmploymentContract(employmentContract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formula)) {
            return false;
        }
        return getId() != null && getId().equals(((Formula) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formula{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", adminFeesPct=" + getAdminFeesPct() +
            ", additionalFeesPct=" + getAdditionalFeesPct() +
            "}";
    }
}
