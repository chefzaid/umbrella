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
 * A Enterprise.
 */
@Entity
@Table(name = "enterprise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "company_status")
    private String companyStatus;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Column(name = "siret")
    private String siret;

    @Column(name = "siren")
    private String siren;

    @Column(name = "sales_tax_number")
    private String salesTaxNumber;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bic_swift")
    private String bicSwift;

    @Column(name = "website")
    private String website;

    @Column(name = "default_invoice_terms")
    private String defaultInvoiceTerms;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "enterprise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "document", "expense", "enterprise" }, allowSetters = true)
    private Set<Parameter> parameters = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "enterprise")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enterprise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Enterprise name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyStatus() {
        return this.companyStatus;
    }

    public Enterprise companyStatus(String companyStatus) {
        this.setCompanyStatus(companyStatus);
        return this;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Enterprise logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Enterprise logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getSiret() {
        return this.siret;
    }

    public Enterprise siret(String siret) {
        this.setSiret(siret);
        return this;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getSiren() {
        return this.siren;
    }

    public Enterprise siren(String siren) {
        this.setSiren(siren);
        return this;
    }

    public void setSiren(String siren) {
        this.siren = siren;
    }

    public String getSalesTaxNumber() {
        return this.salesTaxNumber;
    }

    public Enterprise salesTaxNumber(String salesTaxNumber) {
        this.setSalesTaxNumber(salesTaxNumber);
        return this;
    }

    public void setSalesTaxNumber(String salesTaxNumber) {
        this.salesTaxNumber = salesTaxNumber;
    }

    public String getIban() {
        return this.iban;
    }

    public Enterprise iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBicSwift() {
        return this.bicSwift;
    }

    public Enterprise bicSwift(String bicSwift) {
        this.setBicSwift(bicSwift);
        return this;
    }

    public void setBicSwift(String bicSwift) {
        this.bicSwift = bicSwift;
    }

    public String getWebsite() {
        return this.website;
    }

    public Enterprise website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDefaultInvoiceTerms() {
        return this.defaultInvoiceTerms;
    }

    public Enterprise defaultInvoiceTerms(String defaultInvoiceTerms) {
        this.setDefaultInvoiceTerms(defaultInvoiceTerms);
        return this;
    }

    public void setDefaultInvoiceTerms(String defaultInvoiceTerms) {
        this.defaultInvoiceTerms = defaultInvoiceTerms;
    }

    public Set<Parameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<Parameter> parameters) {
        if (this.parameters != null) {
            this.parameters.forEach(i -> i.setEnterprise(null));
        }
        if (parameters != null) {
            parameters.forEach(i -> i.setEnterprise(this));
        }
        this.parameters = parameters;
    }

    public Enterprise parameters(Set<Parameter> parameters) {
        this.setParameters(parameters);
        return this;
    }

    public Enterprise addParameters(Parameter parameter) {
        this.parameters.add(parameter);
        parameter.setEnterprise(this);
        return this;
    }

    public Enterprise removeParameters(Parameter parameter) {
        this.parameters.remove(parameter);
        parameter.setEnterprise(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setEnterprise(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setEnterprise(this));
        }
        this.documents = documents;
    }

    public Enterprise documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Enterprise addDocuments(Document document) {
        this.documents.add(document);
        document.setEnterprise(this);
        return this;
    }

    public Enterprise removeDocuments(Document document) {
        this.documents.remove(document);
        document.setEnterprise(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enterprise)) {
            return false;
        }
        return getId() != null && getId().equals(((Enterprise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enterprise{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", companyStatus='" + getCompanyStatus() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", siret='" + getSiret() + "'" +
            ", siren='" + getSiren() + "'" +
            ", salesTaxNumber='" + getSalesTaxNumber() + "'" +
            ", iban='" + getIban() + "'" +
            ", bicSwift='" + getBicSwift() + "'" +
            ", website='" + getWebsite() + "'" +
            ", defaultInvoiceTerms='" + getDefaultInvoiceTerms() + "'" +
            "}";
    }
}
