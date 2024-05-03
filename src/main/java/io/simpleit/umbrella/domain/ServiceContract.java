package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceContract.
 */
@Entity
@Table(name = "service_contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "service_label")
    private String serviceLabel;

    @Column(name = "daily_rate")
    private Double dailyRate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "extension_terms")
    private String extensionTerms;

    @Column(name = "signed_by_supplier")
    private Boolean signedBySupplier;

    @Column(name = "signed_by_client")
    private Boolean signedByClient;

    @JsonIgnoreProperties(value = { "address", "contacts", "serviceContract", "project" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Client employee;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceContract")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceContract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee", "documents", "amendments", "serviceContract" }, allowSetters = true)
    private Set<ServiceContract> amendments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "employee", "documents", "amendments", "serviceContract" }, allowSetters = true)
    private ServiceContract serviceContract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceContract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceLabel() {
        return this.serviceLabel;
    }

    public ServiceContract serviceLabel(String serviceLabel) {
        this.setServiceLabel(serviceLabel);
        return this;
    }

    public void setServiceLabel(String serviceLabel) {
        this.serviceLabel = serviceLabel;
    }

    public Double getDailyRate() {
        return this.dailyRate;
    }

    public ServiceContract dailyRate(Double dailyRate) {
        this.setDailyRate(dailyRate);
        return this;
    }

    public void setDailyRate(Double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public ServiceContract startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public ServiceContract endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getExtensionTerms() {
        return this.extensionTerms;
    }

    public ServiceContract extensionTerms(String extensionTerms) {
        this.setExtensionTerms(extensionTerms);
        return this;
    }

    public void setExtensionTerms(String extensionTerms) {
        this.extensionTerms = extensionTerms;
    }

    public Boolean getSignedBySupplier() {
        return this.signedBySupplier;
    }

    public ServiceContract signedBySupplier(Boolean signedBySupplier) {
        this.setSignedBySupplier(signedBySupplier);
        return this;
    }

    public void setSignedBySupplier(Boolean signedBySupplier) {
        this.signedBySupplier = signedBySupplier;
    }

    public Boolean getSignedByClient() {
        return this.signedByClient;
    }

    public ServiceContract signedByClient(Boolean signedByClient) {
        this.setSignedByClient(signedByClient);
        return this;
    }

    public void setSignedByClient(Boolean signedByClient) {
        this.signedByClient = signedByClient;
    }

    public Client getEmployee() {
        return this.employee;
    }

    public void setEmployee(Client client) {
        this.employee = client;
    }

    public ServiceContract employee(Client client) {
        this.setEmployee(client);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setServiceContract(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setServiceContract(this));
        }
        this.documents = documents;
    }

    public ServiceContract documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public ServiceContract addDocuments(Document document) {
        this.documents.add(document);
        document.setServiceContract(this);
        return this;
    }

    public ServiceContract removeDocuments(Document document) {
        this.documents.remove(document);
        document.setServiceContract(null);
        return this;
    }

    public Set<ServiceContract> getAmendments() {
        return this.amendments;
    }

    public void setAmendments(Set<ServiceContract> serviceContracts) {
        if (this.amendments != null) {
            this.amendments.forEach(i -> i.setServiceContract(null));
        }
        if (serviceContracts != null) {
            serviceContracts.forEach(i -> i.setServiceContract(this));
        }
        this.amendments = serviceContracts;
    }

    public ServiceContract amendments(Set<ServiceContract> serviceContracts) {
        this.setAmendments(serviceContracts);
        return this;
    }

    public ServiceContract addAmendments(ServiceContract serviceContract) {
        this.amendments.add(serviceContract);
        serviceContract.setServiceContract(this);
        return this;
    }

    public ServiceContract removeAmendments(ServiceContract serviceContract) {
        this.amendments.remove(serviceContract);
        serviceContract.setServiceContract(null);
        return this;
    }

    public ServiceContract getServiceContract() {
        return this.serviceContract;
    }

    public void setServiceContract(ServiceContract serviceContract) {
        this.serviceContract = serviceContract;
    }

    public ServiceContract serviceContract(ServiceContract serviceContract) {
        this.setServiceContract(serviceContract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceContract)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceContract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceContract{" +
            "id=" + getId() +
            ", serviceLabel='" + getServiceLabel() + "'" +
            ", dailyRate=" + getDailyRate() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", extensionTerms='" + getExtensionTerms() + "'" +
            ", signedBySupplier='" + getSignedBySupplier() + "'" +
            ", signedByClient='" + getSignedByClient() + "'" +
            "}";
    }
}
