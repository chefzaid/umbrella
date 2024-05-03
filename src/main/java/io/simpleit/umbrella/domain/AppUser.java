package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "creation_date")
    private Instant creationDate;

    @JsonIgnoreProperties(value = { "address", "appUser", "client" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Contact contact;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "document", "expense", "enterprise" }, allowSetters = true)
    private Set<Parameter> preferences = new HashSet<>();

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
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public AppUser username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public AppUser password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public AppUser photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public AppUser photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public AppUser isAdmin(Boolean isAdmin) {
        this.setIsAdmin(isAdmin);
        return this;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public AppUser creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public AppUser contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public Set<Parameter> getPreferences() {
        return this.preferences;
    }

    public void setPreferences(Set<Parameter> parameters) {
        if (this.preferences != null) {
            this.preferences.forEach(i -> i.setAppUser(null));
        }
        if (parameters != null) {
            parameters.forEach(i -> i.setAppUser(this));
        }
        this.preferences = parameters;
    }

    public AppUser preferences(Set<Parameter> parameters) {
        this.setPreferences(parameters);
        return this;
    }

    public AppUser addPreferences(Parameter parameter) {
        this.preferences.add(parameter);
        parameter.setAppUser(this);
        return this;
    }

    public AppUser removePreferences(Parameter parameter) {
        this.preferences.remove(parameter);
        parameter.setAppUser(null);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        if (this.employee != null) {
            this.employee.setUser(null);
        }
        if (employee != null) {
            employee.setUser(this);
        }
        this.employee = employee;
    }

    public AppUser employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return getId() != null && getId().equals(((AppUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", isAdmin='" + getIsAdmin() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
