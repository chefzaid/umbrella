package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.simpleit.umbrella.domain.enumeration.Gender;
import io.simpleit.umbrella.domain.enumeration.MaritalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "employee_number", nullable = false)
    private Long employeeNumber;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Column(name = "birth_place", nullable = false)
    private String birthPlace;

    @Column(name = "nationality")
    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_children_number")
    private Long dependentChildrenNumber;

    @Column(name = "social_security_number")
    private String socialSecurityNumber;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bic_swift")
    private String bicSwift;

    @JsonIgnoreProperties(value = { "contact", "preferences", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AppUser user;

    @JsonIgnoreProperties(value = { "contact", "employee", "client" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Address address;

    @JsonIgnoreProperties(value = { "forumula", "documents", "amendments", "employee", "employmentContract" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private EmploymentContract contract;

    @JsonIgnoreProperties(value = { "document", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private IdDocument idDocument;

    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Wallet wallet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "employee", "timeSheet", "expense", "invoice" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "project", "document", "lines", "employee" }, allowSetters = true)
    private Set<TimeSheet> timesheets = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mileageAllowance", "document", "expenses", "employee" }, allowSetters = true)
    private Set<ExpenseNote> expenseNotes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operations", "employee" }, allowSetters = true)
    private Set<ActivityReport> activityReports = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "document", "employee" }, allowSetters = true)
    private Set<PaySlip> paySlips = new HashSet<>();

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
    private Employee manager;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeNumber() {
        return this.employeeNumber;
    }

    public Employee employeeNumber(Long employeeNumber) {
        this.setEmployeeNumber(employeeNumber);
        return this;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Employee birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public Employee birthPlace(String birthPlace) {
        this.setBirthPlace(birthPlace);
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Employee nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Employee gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public Employee maritalStatus(MaritalStatus maritalStatus) {
        this.setMaritalStatus(maritalStatus);
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Long getDependentChildrenNumber() {
        return this.dependentChildrenNumber;
    }

    public Employee dependentChildrenNumber(Long dependentChildrenNumber) {
        this.setDependentChildrenNumber(dependentChildrenNumber);
        return this;
    }

    public void setDependentChildrenNumber(Long dependentChildrenNumber) {
        this.dependentChildrenNumber = dependentChildrenNumber;
    }

    public String getSocialSecurityNumber() {
        return this.socialSecurityNumber;
    }

    public Employee socialSecurityNumber(String socialSecurityNumber) {
        this.setSocialSecurityNumber(socialSecurityNumber);
        return this;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getIban() {
        return this.iban;
    }

    public Employee iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBicSwift() {
        return this.bicSwift;
    }

    public Employee bicSwift(String bicSwift) {
        this.setBicSwift(bicSwift);
        return this;
    }

    public void setBicSwift(String bicSwift) {
        this.bicSwift = bicSwift;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser appUser) {
        this.user = appUser;
    }

    public Employee user(AppUser appUser) {
        this.setUser(appUser);
        return this;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Employee address(Address address) {
        this.setAddress(address);
        return this;
    }

    public EmploymentContract getContract() {
        return this.contract;
    }

    public void setContract(EmploymentContract employmentContract) {
        this.contract = employmentContract;
    }

    public Employee contract(EmploymentContract employmentContract) {
        this.setContract(employmentContract);
        return this;
    }

    public IdDocument getIdDocument() {
        return this.idDocument;
    }

    public void setIdDocument(IdDocument idDocument) {
        this.idDocument = idDocument;
    }

    public Employee idDocument(IdDocument idDocument) {
        this.setIdDocument(idDocument);
        return this;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Employee wallet(Wallet wallet) {
        this.setWallet(wallet);
        return this;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.setEmployee(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setEmployee(this));
        }
        this.projects = projects;
    }

    public Employee projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Employee addProject(Project project) {
        this.projects.add(project);
        project.setEmployee(this);
        return this;
    }

    public Employee removeProject(Project project) {
        this.projects.remove(project);
        project.setEmployee(null);
        return this;
    }

    public Set<TimeSheet> getTimesheets() {
        return this.timesheets;
    }

    public void setTimesheets(Set<TimeSheet> timeSheets) {
        if (this.timesheets != null) {
            this.timesheets.forEach(i -> i.setEmployee(null));
        }
        if (timeSheets != null) {
            timeSheets.forEach(i -> i.setEmployee(this));
        }
        this.timesheets = timeSheets;
    }

    public Employee timesheets(Set<TimeSheet> timeSheets) {
        this.setTimesheets(timeSheets);
        return this;
    }

    public Employee addTimesheets(TimeSheet timeSheet) {
        this.timesheets.add(timeSheet);
        timeSheet.setEmployee(this);
        return this;
    }

    public Employee removeTimesheets(TimeSheet timeSheet) {
        this.timesheets.remove(timeSheet);
        timeSheet.setEmployee(null);
        return this;
    }

    public Set<ExpenseNote> getExpenseNotes() {
        return this.expenseNotes;
    }

    public void setExpenseNotes(Set<ExpenseNote> expenseNotes) {
        if (this.expenseNotes != null) {
            this.expenseNotes.forEach(i -> i.setEmployee(null));
        }
        if (expenseNotes != null) {
            expenseNotes.forEach(i -> i.setEmployee(this));
        }
        this.expenseNotes = expenseNotes;
    }

    public Employee expenseNotes(Set<ExpenseNote> expenseNotes) {
        this.setExpenseNotes(expenseNotes);
        return this;
    }

    public Employee addExpenseNotes(ExpenseNote expenseNote) {
        this.expenseNotes.add(expenseNote);
        expenseNote.setEmployee(this);
        return this;
    }

    public Employee removeExpenseNotes(ExpenseNote expenseNote) {
        this.expenseNotes.remove(expenseNote);
        expenseNote.setEmployee(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setEmployee(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setEmployee(this));
        }
        this.documents = documents;
    }

    public Employee documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Employee addDocuments(Document document) {
        this.documents.add(document);
        document.setEmployee(this);
        return this;
    }

    public Employee removeDocuments(Document document) {
        this.documents.remove(document);
        document.setEmployee(null);
        return this;
    }

    public Set<ActivityReport> getActivityReports() {
        return this.activityReports;
    }

    public void setActivityReports(Set<ActivityReport> activityReports) {
        if (this.activityReports != null) {
            this.activityReports.forEach(i -> i.setEmployee(null));
        }
        if (activityReports != null) {
            activityReports.forEach(i -> i.setEmployee(this));
        }
        this.activityReports = activityReports;
    }

    public Employee activityReports(Set<ActivityReport> activityReports) {
        this.setActivityReports(activityReports);
        return this;
    }

    public Employee addActivityReports(ActivityReport activityReport) {
        this.activityReports.add(activityReport);
        activityReport.setEmployee(this);
        return this;
    }

    public Employee removeActivityReports(ActivityReport activityReport) {
        this.activityReports.remove(activityReport);
        activityReport.setEmployee(null);
        return this;
    }

    public Set<PaySlip> getPaySlips() {
        return this.paySlips;
    }

    public void setPaySlips(Set<PaySlip> paySlips) {
        if (this.paySlips != null) {
            this.paySlips.forEach(i -> i.setEmployee(null));
        }
        if (paySlips != null) {
            paySlips.forEach(i -> i.setEmployee(this));
        }
        this.paySlips = paySlips;
    }

    public Employee paySlips(Set<PaySlip> paySlips) {
        this.setPaySlips(paySlips);
        return this;
    }

    public Employee addPaySlips(PaySlip paySlip) {
        this.paySlips.add(paySlip);
        paySlip.setEmployee(this);
        return this;
    }

    public Employee removePaySlips(PaySlip paySlip) {
        this.paySlips.remove(paySlip);
        paySlip.setEmployee(null);
        return this;
    }

    public Employee getManager() {
        return this.manager;
    }

    public void setManager(Employee employee) {
        this.manager = employee;
    }

    public Employee manager(Employee employee) {
        this.setManager(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", employeeNumber=" + getEmployeeNumber() +
            ", birthDate='" + getBirthDate() + "'" +
            ", birthPlace='" + getBirthPlace() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", gender='" + getGender() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", dependentChildrenNumber=" + getDependentChildrenNumber() +
            ", socialSecurityNumber='" + getSocialSecurityNumber() + "'" +
            ", iban='" + getIban() + "'" +
            ", bicSwift='" + getBicSwift() + "'" +
            "}";
    }
}
