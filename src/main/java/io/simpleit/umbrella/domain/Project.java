package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.simpleit.umbrella.domain.enumeration.ProjectState;
import io.simpleit.umbrella.domain.enumeration.ProjectType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProjectType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ProjectState state;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "daily_rate", nullable = false)
    private Long dailyRate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "remote_work_pct")
    private Double remoteWorkPct;

    @JsonIgnoreProperties(value = { "address", "contacts", "serviceContract", "project" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Client client;

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

    @JsonIgnoreProperties(value = { "project", "document", "lines", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "project")
    private TimeSheet timeSheet;

    @JsonIgnoreProperties(value = { "paymentMethod", "project", "expenseNote" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "project")
    private Expense expense;

    @JsonIgnoreProperties(value = { "poject", "document", "lines" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "poject")
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Project id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectType getType() {
        return this.type;
    }

    public Project type(ProjectType type) {
        this.setType(type);
        return this;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    public ProjectState getState() {
        return this.state;
    }

    public Project state(ProjectState state) {
        this.setState(state);
        return this;
    }

    public void setState(ProjectState state) {
        this.state = state;
    }

    public String getTitle() {
        return this.title;
    }

    public Project title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Project description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDailyRate() {
        return this.dailyRate;
    }

    public Project dailyRate(Long dailyRate) {
        this.setDailyRate(dailyRate);
        return this;
    }

    public void setDailyRate(Long dailyRate) {
        this.dailyRate = dailyRate;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Project startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Project endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getRemoteWorkPct() {
        return this.remoteWorkPct;
    }

    public Project remoteWorkPct(Double remoteWorkPct) {
        this.setRemoteWorkPct(remoteWorkPct);
        return this;
    }

    public void setRemoteWorkPct(Double remoteWorkPct) {
        this.remoteWorkPct = remoteWorkPct;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Project client(Client client) {
        this.setClient(client);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public TimeSheet getTimeSheet() {
        return this.timeSheet;
    }

    public void setTimeSheet(TimeSheet timeSheet) {
        if (this.timeSheet != null) {
            this.timeSheet.setProject(null);
        }
        if (timeSheet != null) {
            timeSheet.setProject(this);
        }
        this.timeSheet = timeSheet;
    }

    public Project timeSheet(TimeSheet timeSheet) {
        this.setTimeSheet(timeSheet);
        return this;
    }

    public Expense getExpense() {
        return this.expense;
    }

    public void setExpense(Expense expense) {
        if (this.expense != null) {
            this.expense.setProject(null);
        }
        if (expense != null) {
            expense.setProject(this);
        }
        this.expense = expense;
    }

    public Project expense(Expense expense) {
        this.setExpense(expense);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        if (this.invoice != null) {
            this.invoice.setPoject(null);
        }
        if (invoice != null) {
            invoice.setPoject(this);
        }
        this.invoice = invoice;
    }

    public Project invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return getId() != null && getId().equals(((Project) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", state='" + getState() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", dailyRate=" + getDailyRate() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", remoteWorkPct=" + getRemoteWorkPct() +
            "}";
    }
}
