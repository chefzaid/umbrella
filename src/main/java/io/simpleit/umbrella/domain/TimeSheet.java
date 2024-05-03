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
 * A TimeSheet.
 */
@Entity
@Table(name = "time_sheet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "concerned_month", nullable = false)
    private String concernedMonth;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "submit_date")
    private Instant submitDate;

    @Column(name = "validation_date")
    private Instant validationDate;

    @JsonIgnoreProperties(value = { "client", "employee", "timeSheet", "expense", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Project project;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "timeSheet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "timeSheet" }, allowSetters = true)
    private Set<TimeSheetLine> lines = new HashSet<>();

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

    public TimeSheet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcernedMonth() {
        return this.concernedMonth;
    }

    public TimeSheet concernedMonth(String concernedMonth) {
        this.setConcernedMonth(concernedMonth);
        return this;
    }

    public void setConcernedMonth(String concernedMonth) {
        this.concernedMonth = concernedMonth;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public TimeSheet creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getSubmitDate() {
        return this.submitDate;
    }

    public TimeSheet submitDate(Instant submitDate) {
        this.setSubmitDate(submitDate);
        return this;
    }

    public void setSubmitDate(Instant submitDate) {
        this.submitDate = submitDate;
    }

    public Instant getValidationDate() {
        return this.validationDate;
    }

    public TimeSheet validationDate(Instant validationDate) {
        this.setValidationDate(validationDate);
        return this;
    }

    public void setValidationDate(Instant validationDate) {
        this.validationDate = validationDate;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public TimeSheet project(Project project) {
        this.setProject(project);
        return this;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public TimeSheet document(Document document) {
        this.setDocument(document);
        return this;
    }

    public Set<TimeSheetLine> getLines() {
        return this.lines;
    }

    public void setLines(Set<TimeSheetLine> timeSheetLines) {
        if (this.lines != null) {
            this.lines.forEach(i -> i.setTimeSheet(null));
        }
        if (timeSheetLines != null) {
            timeSheetLines.forEach(i -> i.setTimeSheet(this));
        }
        this.lines = timeSheetLines;
    }

    public TimeSheet lines(Set<TimeSheetLine> timeSheetLines) {
        this.setLines(timeSheetLines);
        return this;
    }

    public TimeSheet addLines(TimeSheetLine timeSheetLine) {
        this.lines.add(timeSheetLine);
        timeSheetLine.setTimeSheet(this);
        return this;
    }

    public TimeSheet removeLines(TimeSheetLine timeSheetLine) {
        this.lines.remove(timeSheetLine);
        timeSheetLine.setTimeSheet(null);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TimeSheet employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSheet)) {
            return false;
        }
        return getId() != null && getId().equals(((TimeSheet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSheet{" +
            "id=" + getId() +
            ", concernedMonth='" + getConcernedMonth() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", submitDate='" + getSubmitDate() + "'" +
            ", validationDate='" + getValidationDate() + "'" +
            "}";
    }
}
