package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.simpleit.umbrella.domain.enumeration.EmploymentContractType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmploymentContract.
 */
@Entity
@Table(name = "employment_contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmploymentContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EmploymentContractType type;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "yearly_work_days")
    private Long yearlyWorkDays;

    @Column(name = "signed_by_employer")
    private Boolean signedByEmployer;

    @Column(name = "signed_by_employee")
    private Boolean signedByEmployee;

    @JsonIgnoreProperties(value = { "prospect", "employmentContract" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Formula forumula;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employmentContract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Document> documents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employmentContract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "forumula", "documents", "amendments", "employee", "employmentContract" }, allowSetters = true)
    private Set<EmploymentContract> amendments = new HashSet<>();

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
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contract")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "forumula", "documents", "amendments", "employee", "employmentContract" }, allowSetters = true)
    private EmploymentContract employmentContract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmploymentContract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmploymentContractType getType() {
        return this.type;
    }

    public EmploymentContract type(EmploymentContractType type) {
        this.setType(type);
        return this;
    }

    public void setType(EmploymentContractType type) {
        this.type = type;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public EmploymentContract jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDate getHireDate() {
        return this.hireDate;
    }

    public EmploymentContract hireDate(LocalDate hireDate) {
        this.setHireDate(hireDate);
        return this;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Double getSalary() {
        return this.salary;
    }

    public EmploymentContract salary(Double salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getYearlyWorkDays() {
        return this.yearlyWorkDays;
    }

    public EmploymentContract yearlyWorkDays(Long yearlyWorkDays) {
        this.setYearlyWorkDays(yearlyWorkDays);
        return this;
    }

    public void setYearlyWorkDays(Long yearlyWorkDays) {
        this.yearlyWorkDays = yearlyWorkDays;
    }

    public Boolean getSignedByEmployer() {
        return this.signedByEmployer;
    }

    public EmploymentContract signedByEmployer(Boolean signedByEmployer) {
        this.setSignedByEmployer(signedByEmployer);
        return this;
    }

    public void setSignedByEmployer(Boolean signedByEmployer) {
        this.signedByEmployer = signedByEmployer;
    }

    public Boolean getSignedByEmployee() {
        return this.signedByEmployee;
    }

    public EmploymentContract signedByEmployee(Boolean signedByEmployee) {
        this.setSignedByEmployee(signedByEmployee);
        return this;
    }

    public void setSignedByEmployee(Boolean signedByEmployee) {
        this.signedByEmployee = signedByEmployee;
    }

    public Formula getForumula() {
        return this.forumula;
    }

    public void setForumula(Formula formula) {
        this.forumula = formula;
    }

    public EmploymentContract forumula(Formula formula) {
        this.setForumula(formula);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setEmploymentContract(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setEmploymentContract(this));
        }
        this.documents = documents;
    }

    public EmploymentContract documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public EmploymentContract addDocuments(Document document) {
        this.documents.add(document);
        document.setEmploymentContract(this);
        return this;
    }

    public EmploymentContract removeDocuments(Document document) {
        this.documents.remove(document);
        document.setEmploymentContract(null);
        return this;
    }

    public Set<EmploymentContract> getAmendments() {
        return this.amendments;
    }

    public void setAmendments(Set<EmploymentContract> employmentContracts) {
        if (this.amendments != null) {
            this.amendments.forEach(i -> i.setEmploymentContract(null));
        }
        if (employmentContracts != null) {
            employmentContracts.forEach(i -> i.setEmploymentContract(this));
        }
        this.amendments = employmentContracts;
    }

    public EmploymentContract amendments(Set<EmploymentContract> employmentContracts) {
        this.setAmendments(employmentContracts);
        return this;
    }

    public EmploymentContract addAmendments(EmploymentContract employmentContract) {
        this.amendments.add(employmentContract);
        employmentContract.setEmploymentContract(this);
        return this;
    }

    public EmploymentContract removeAmendments(EmploymentContract employmentContract) {
        this.amendments.remove(employmentContract);
        employmentContract.setEmploymentContract(null);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        if (this.employee != null) {
            this.employee.setContract(null);
        }
        if (employee != null) {
            employee.setContract(this);
        }
        this.employee = employee;
    }

    public EmploymentContract employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public EmploymentContract getEmploymentContract() {
        return this.employmentContract;
    }

    public void setEmploymentContract(EmploymentContract employmentContract) {
        this.employmentContract = employmentContract;
    }

    public EmploymentContract employmentContract(EmploymentContract employmentContract) {
        this.setEmploymentContract(employmentContract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmploymentContract)) {
            return false;
        }
        return getId() != null && getId().equals(((EmploymentContract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmploymentContract{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", salary=" + getSalary() +
            ", yearlyWorkDays=" + getYearlyWorkDays() +
            ", signedByEmployer='" + getSignedByEmployer() + "'" +
            ", signedByEmployee='" + getSignedByEmployee() + "'" +
            "}";
    }
}
