package io.simpleit.umbrella.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "read_date")
    private Instant readDate;

    @Column(name = "hide")
    private Boolean hide;

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
    private Employee author;

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
    private Employee receiver;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Notification title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Notification description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Notification creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getReadDate() {
        return this.readDate;
    }

    public Notification readDate(Instant readDate) {
        this.setReadDate(readDate);
        return this;
    }

    public void setReadDate(Instant readDate) {
        this.readDate = readDate;
    }

    public Boolean getHide() {
        return this.hide;
    }

    public Notification hide(Boolean hide) {
        this.setHide(hide);
        return this;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Employee getAuthor() {
        return this.author;
    }

    public void setAuthor(Employee employee) {
        this.author = employee;
    }

    public Notification author(Employee employee) {
        this.setAuthor(employee);
        return this;
    }

    public Employee getReceiver() {
        return this.receiver;
    }

    public void setReceiver(Employee employee) {
        this.receiver = employee;
    }

    public Notification receiver(Employee employee) {
        this.setReceiver(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", readDate='" + getReadDate() + "'" +
            ", hide='" + getHide() + "'" +
            "}";
    }
}
