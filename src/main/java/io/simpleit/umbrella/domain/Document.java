package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "upload_date")
    private Instant uploadDate;

    @Column(name = "issuing_date")
    private Instant issuingDate;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @Lob
    @Column(name = "file", nullable = false)
    private byte[] file;

    @NotNull
    @Column(name = "file_content_type", nullable = false)
    private String fileContentType;

    @JsonIgnoreProperties(value = { "appUser", "document", "expense", "enterprise" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Parameter documentType;

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

    @JsonIgnoreProperties(value = { "document", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "document")
    private IdDocument idDocument;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "forumula", "documents", "amendments", "employee", "employmentContract" }, allowSetters = true)
    private EmploymentContract employmentContract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "employee", "documents", "amendments", "serviceContract" }, allowSetters = true)
    private ServiceContract serviceContract;

    @JsonIgnoreProperties(value = { "project", "document", "lines", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "document")
    private TimeSheet timeSheet;

    @JsonIgnoreProperties(value = { "mileageAllowance", "document", "expenses", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "document")
    private ExpenseNote expenseNote;

    @JsonIgnoreProperties(value = { "poject", "document", "lines" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "document")
    private Invoice invoice;

    @JsonIgnoreProperties(value = { "document", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "document")
    private PaySlip paySlip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parameters", "documents" }, allowSetters = true)
    private Enterprise enterprise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Document id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Document label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Instant getUploadDate() {
        return this.uploadDate;
    }

    public Document uploadDate(Instant uploadDate) {
        this.setUploadDate(uploadDate);
        return this;
    }

    public void setUploadDate(Instant uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Instant getIssuingDate() {
        return this.issuingDate;
    }

    public Document issuingDate(Instant issuingDate) {
        this.setIssuingDate(issuingDate);
        return this;
    }

    public void setIssuingDate(Instant issuingDate) {
        this.issuingDate = issuingDate;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public Document expirationDate(Instant expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public byte[] getFile() {
        return this.file;
    }

    public Document file(byte[] file) {
        this.setFile(file);
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public Document fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Parameter getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(Parameter parameter) {
        this.documentType = parameter;
    }

    public Document documentType(Parameter parameter) {
        this.setDocumentType(parameter);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Document employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public IdDocument getIdDocument() {
        return this.idDocument;
    }

    public void setIdDocument(IdDocument idDocument) {
        if (this.idDocument != null) {
            this.idDocument.setDocument(null);
        }
        if (idDocument != null) {
            idDocument.setDocument(this);
        }
        this.idDocument = idDocument;
    }

    public Document idDocument(IdDocument idDocument) {
        this.setIdDocument(idDocument);
        return this;
    }

    public EmploymentContract getEmploymentContract() {
        return this.employmentContract;
    }

    public void setEmploymentContract(EmploymentContract employmentContract) {
        this.employmentContract = employmentContract;
    }

    public Document employmentContract(EmploymentContract employmentContract) {
        this.setEmploymentContract(employmentContract);
        return this;
    }

    public ServiceContract getServiceContract() {
        return this.serviceContract;
    }

    public void setServiceContract(ServiceContract serviceContract) {
        this.serviceContract = serviceContract;
    }

    public Document serviceContract(ServiceContract serviceContract) {
        this.setServiceContract(serviceContract);
        return this;
    }

    public TimeSheet getTimeSheet() {
        return this.timeSheet;
    }

    public void setTimeSheet(TimeSheet timeSheet) {
        if (this.timeSheet != null) {
            this.timeSheet.setDocument(null);
        }
        if (timeSheet != null) {
            timeSheet.setDocument(this);
        }
        this.timeSheet = timeSheet;
    }

    public Document timeSheet(TimeSheet timeSheet) {
        this.setTimeSheet(timeSheet);
        return this;
    }

    public ExpenseNote getExpenseNote() {
        return this.expenseNote;
    }

    public void setExpenseNote(ExpenseNote expenseNote) {
        if (this.expenseNote != null) {
            this.expenseNote.setDocument(null);
        }
        if (expenseNote != null) {
            expenseNote.setDocument(this);
        }
        this.expenseNote = expenseNote;
    }

    public Document expenseNote(ExpenseNote expenseNote) {
        this.setExpenseNote(expenseNote);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        if (this.invoice != null) {
            this.invoice.setDocument(null);
        }
        if (invoice != null) {
            invoice.setDocument(this);
        }
        this.invoice = invoice;
    }

    public Document invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public PaySlip getPaySlip() {
        return this.paySlip;
    }

    public void setPaySlip(PaySlip paySlip) {
        if (this.paySlip != null) {
            this.paySlip.setDocument(null);
        }
        if (paySlip != null) {
            paySlip.setDocument(this);
        }
        this.paySlip = paySlip;
    }

    public Document paySlip(PaySlip paySlip) {
        this.setPaySlip(paySlip);
        return this;
    }

    public Enterprise getEnterprise() {
        return this.enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Document enterprise(Enterprise enterprise) {
        this.setEnterprise(enterprise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return getId() != null && getId().equals(((Document) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", issuingDate='" + getIssuingDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            "}";
    }
}
