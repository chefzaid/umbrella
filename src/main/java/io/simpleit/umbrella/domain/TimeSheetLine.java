package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TimeSheetLine.
 */
@Entity
@Table(name = "time_sheet_line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeSheetLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "monthly_days", nullable = false)
    private Double monthlyDays;

    @Column(name = "extra_hours")
    private Double extraHours;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "project", "document", "lines", "employee" }, allowSetters = true)
    private TimeSheet timeSheet;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TimeSheetLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMonthlyDays() {
        return this.monthlyDays;
    }

    public TimeSheetLine monthlyDays(Double monthlyDays) {
        this.setMonthlyDays(monthlyDays);
        return this;
    }

    public void setMonthlyDays(Double monthlyDays) {
        this.monthlyDays = monthlyDays;
    }

    public Double getExtraHours() {
        return this.extraHours;
    }

    public TimeSheetLine extraHours(Double extraHours) {
        this.setExtraHours(extraHours);
        return this;
    }

    public void setExtraHours(Double extraHours) {
        this.extraHours = extraHours;
    }

    public String getComments() {
        return this.comments;
    }

    public TimeSheetLine comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public TimeSheet getTimeSheet() {
        return this.timeSheet;
    }

    public void setTimeSheet(TimeSheet timeSheet) {
        this.timeSheet = timeSheet;
    }

    public TimeSheetLine timeSheet(TimeSheet timeSheet) {
        this.setTimeSheet(timeSheet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSheetLine)) {
            return false;
        }
        return getId() != null && getId().equals(((TimeSheetLine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSheetLine{" +
            "id=" + getId() +
            ", monthlyDays=" + getMonthlyDays() +
            ", extraHours=" + getExtraHours() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
