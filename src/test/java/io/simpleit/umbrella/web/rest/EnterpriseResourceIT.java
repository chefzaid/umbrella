package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.EnterpriseAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.Enterprise;
import io.simpleit.umbrella.repository.EnterpriseRepository;
import jakarta.persistence.EntityManager;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EnterpriseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnterpriseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_STATUS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_SIRET = "AAAAAAAAAA";
    private static final String UPDATED_SIRET = "BBBBBBBBBB";

    private static final String DEFAULT_SIREN = "AAAAAAAAAA";
    private static final String UPDATED_SIREN = "BBBBBBBBBB";

    private static final String DEFAULT_SALES_TAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SALES_TAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_BIC_SWIFT = "AAAAAAAAAA";
    private static final String UPDATED_BIC_SWIFT = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_INVOICE_TERMS = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_INVOICE_TERMS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enterprises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnterpriseMockMvc;

    private Enterprise enterprise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enterprise createEntity(EntityManager em) {
        Enterprise enterprise = new Enterprise()
            .name(DEFAULT_NAME)
            .companyStatus(DEFAULT_COMPANY_STATUS)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .siret(DEFAULT_SIRET)
            .siren(DEFAULT_SIREN)
            .salesTaxNumber(DEFAULT_SALES_TAX_NUMBER)
            .iban(DEFAULT_IBAN)
            .bicSwift(DEFAULT_BIC_SWIFT)
            .website(DEFAULT_WEBSITE)
            .defaultInvoiceTerms(DEFAULT_DEFAULT_INVOICE_TERMS);
        return enterprise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enterprise createUpdatedEntity(EntityManager em) {
        Enterprise enterprise = new Enterprise()
            .name(UPDATED_NAME)
            .companyStatus(UPDATED_COMPANY_STATUS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .siret(UPDATED_SIRET)
            .siren(UPDATED_SIREN)
            .salesTaxNumber(UPDATED_SALES_TAX_NUMBER)
            .iban(UPDATED_IBAN)
            .bicSwift(UPDATED_BIC_SWIFT)
            .website(UPDATED_WEBSITE)
            .defaultInvoiceTerms(UPDATED_DEFAULT_INVOICE_TERMS);
        return enterprise;
    }

    @BeforeEach
    public void initTest() {
        enterprise = createEntity(em);
    }

    @Test
    @Transactional
    void createEnterprise() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enterprise
        var returnedEnterprise = om.readValue(
            restEnterpriseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterprise)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Enterprise.class
        );

        // Validate the Enterprise in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEnterpriseUpdatableFieldsEquals(returnedEnterprise, getPersistedEnterprise(returnedEnterprise));
    }

    @Test
    @Transactional
    void createEnterpriseWithExistingId() throws Exception {
        // Create the Enterprise with an existing ID
        enterprise.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterprise)))
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnterprises() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        // Get all the enterpriseList
        restEnterpriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enterprise.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].companyStatus").value(hasItem(DEFAULT_COMPANY_STATUS)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].siret").value(hasItem(DEFAULT_SIRET)))
            .andExpect(jsonPath("$.[*].siren").value(hasItem(DEFAULT_SIREN)))
            .andExpect(jsonPath("$.[*].salesTaxNumber").value(hasItem(DEFAULT_SALES_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].bicSwift").value(hasItem(DEFAULT_BIC_SWIFT)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].defaultInvoiceTerms").value(hasItem(DEFAULT_DEFAULT_INVOICE_TERMS)));
    }

    @Test
    @Transactional
    void getEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        // Get the enterprise
        restEnterpriseMockMvc
            .perform(get(ENTITY_API_URL_ID, enterprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enterprise.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.companyStatus").value(DEFAULT_COMPANY_STATUS))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64.getEncoder().encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.siret").value(DEFAULT_SIRET))
            .andExpect(jsonPath("$.siren").value(DEFAULT_SIREN))
            .andExpect(jsonPath("$.salesTaxNumber").value(DEFAULT_SALES_TAX_NUMBER))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.bicSwift").value(DEFAULT_BIC_SWIFT))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.defaultInvoiceTerms").value(DEFAULT_DEFAULT_INVOICE_TERMS));
    }

    @Test
    @Transactional
    void getNonExistingEnterprise() throws Exception {
        // Get the enterprise
        restEnterpriseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enterprise
        Enterprise updatedEnterprise = enterpriseRepository.findById(enterprise.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnterprise are not directly saved in db
        em.detach(updatedEnterprise);
        updatedEnterprise
            .name(UPDATED_NAME)
            .companyStatus(UPDATED_COMPANY_STATUS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .siret(UPDATED_SIRET)
            .siren(UPDATED_SIREN)
            .salesTaxNumber(UPDATED_SALES_TAX_NUMBER)
            .iban(UPDATED_IBAN)
            .bicSwift(UPDATED_BIC_SWIFT)
            .website(UPDATED_WEBSITE)
            .defaultInvoiceTerms(UPDATED_DEFAULT_INVOICE_TERMS);

        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnterprise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnterpriseToMatchAllProperties(updatedEnterprise);
    }

    @Test
    @Transactional
    void putNonExistingEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enterprise.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterprise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enterprise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterprise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnterpriseWithPatch() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enterprise using partial update
        Enterprise partialUpdatedEnterprise = new Enterprise();
        partialUpdatedEnterprise.setId(enterprise.getId());

        partialUpdatedEnterprise
            .companyStatus(UPDATED_COMPANY_STATUS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .siren(UPDATED_SIREN)
            .salesTaxNumber(UPDATED_SALES_TAX_NUMBER)
            .iban(UPDATED_IBAN)
            .bicSwift(UPDATED_BIC_SWIFT)
            .website(UPDATED_WEBSITE)
            .defaultInvoiceTerms(UPDATED_DEFAULT_INVOICE_TERMS);

        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnterpriseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnterprise, enterprise),
            getPersistedEnterprise(enterprise)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnterpriseWithPatch() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enterprise using partial update
        Enterprise partialUpdatedEnterprise = new Enterprise();
        partialUpdatedEnterprise.setId(enterprise.getId());

        partialUpdatedEnterprise
            .name(UPDATED_NAME)
            .companyStatus(UPDATED_COMPANY_STATUS)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .siret(UPDATED_SIRET)
            .siren(UPDATED_SIREN)
            .salesTaxNumber(UPDATED_SALES_TAX_NUMBER)
            .iban(UPDATED_IBAN)
            .bicSwift(UPDATED_BIC_SWIFT)
            .website(UPDATED_WEBSITE)
            .defaultInvoiceTerms(UPDATED_DEFAULT_INVOICE_TERMS);

        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnterpriseUpdatableFieldsEquals(partialUpdatedEnterprise, getPersistedEnterprise(partialUpdatedEnterprise));
    }

    @Test
    @Transactional
    void patchNonExistingEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enterprise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enterprise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enterprise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.saveAndFlush(enterprise);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enterprise
        restEnterpriseMockMvc
            .perform(delete(ENTITY_API_URL_ID, enterprise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enterpriseRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Enterprise getPersistedEnterprise(Enterprise enterprise) {
        return enterpriseRepository.findById(enterprise.getId()).orElseThrow();
    }

    protected void assertPersistedEnterpriseToMatchAllProperties(Enterprise expectedEnterprise) {
        assertEnterpriseAllPropertiesEquals(expectedEnterprise, getPersistedEnterprise(expectedEnterprise));
    }

    protected void assertPersistedEnterpriseToMatchUpdatableProperties(Enterprise expectedEnterprise) {
        assertEnterpriseAllUpdatablePropertiesEquals(expectedEnterprise, getPersistedEnterprise(expectedEnterprise));
    }
}
