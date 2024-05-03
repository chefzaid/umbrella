package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "company_status")
    private String companyStatus;

    @JsonIgnoreProperties(value = { "contact", "employee", "client" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "address", "appUser", "client" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @JsonIgnoreProperties(value = { "employee", "documents", "amendments", "serviceContract" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employee")
    private ServiceContract serviceContract;

    @JsonIgnoreProperties(value = { "client", "employee", "timeSheet", "expense", "invoice" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "client")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Client name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyStatus() {
        return this.companyStatus;
    }

    public Client companyStatus(String companyStatus) {
        this.setCompanyStatus(companyStatus);
        return this;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Client address(Address address) {
        this.setAddress(address);
        return this;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setClient(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setClient(this));
        }
        this.contacts = contacts;
    }

    public Client contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Client addContacts(Contact contact) {
        this.contacts.add(contact);
        contact.setClient(this);
        return this;
    }

    public Client removeContacts(Contact contact) {
        this.contacts.remove(contact);
        contact.setClient(null);
        return this;
    }

    public ServiceContract getServiceContract() {
        return this.serviceContract;
    }

    public void setServiceContract(ServiceContract serviceContract) {
        if (this.serviceContract != null) {
            this.serviceContract.setEmployee(null);
        }
        if (serviceContract != null) {
            serviceContract.setEmployee(this);
        }
        this.serviceContract = serviceContract;
    }

    public Client serviceContract(ServiceContract serviceContract) {
        this.setServiceContract(serviceContract);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        if (this.project != null) {
            this.project.setClient(null);
        }
        if (project != null) {
            project.setClient(this);
        }
        this.project = project;
    }

    public Client project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return getId() != null && getId().equals(((Client) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", companyStatus='" + getCompanyStatus() + "'" +
            "}";
    }
}
