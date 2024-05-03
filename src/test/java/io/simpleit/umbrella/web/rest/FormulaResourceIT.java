package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.FormulaAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.Formula;
import io.simpleit.umbrella.repository.FormulaRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link FormulaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormulaResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Double DEFAULT_ADMIN_FEES_PCT = 1D;
    private static final Double UPDATED_ADMIN_FEES_PCT = 2D;

    private static final Double DEFAULT_ADDITIONAL_FEES_PCT = 1D;
    private static final Double UPDATED_ADDITIONAL_FEES_PCT = 2D;

    private static final String ENTITY_API_URL = "/api/formulas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormulaMockMvc;

    private Formula formula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createEntity(EntityManager em) {
        Formula formula = new Formula()
            .label(DEFAULT_LABEL)
            .adminFeesPct(DEFAULT_ADMIN_FEES_PCT)
            .additionalFeesPct(DEFAULT_ADDITIONAL_FEES_PCT);
        return formula;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createUpdatedEntity(EntityManager em) {
        Formula formula = new Formula()
            .label(UPDATED_LABEL)
            .adminFeesPct(UPDATED_ADMIN_FEES_PCT)
            .additionalFeesPct(UPDATED_ADDITIONAL_FEES_PCT);
        return formula;
    }

    @BeforeEach
    public void initTest() {
        formula = createEntity(em);
    }

    @Test
    @Transactional
    void createFormula() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Formula
        var returnedFormula = om.readValue(
            restFormulaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formula)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Formula.class
        );

        // Validate the Formula in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFormulaUpdatableFieldsEquals(returnedFormula, getPersistedFormula(returnedFormula));
    }

    @Test
    @Transactional
    void createFormulaWithExistingId() throws Exception {
        // Create the Formula with an existing ID
        formula.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formula)))
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormulas() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formula.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].adminFeesPct").value(hasItem(DEFAULT_ADMIN_FEES_PCT.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalFeesPct").value(hasItem(DEFAULT_ADDITIONAL_FEES_PCT.doubleValue())));
    }

    @Test
    @Transactional
    void getFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get the formula
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL_ID, formula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formula.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.adminFeesPct").value(DEFAULT_ADMIN_FEES_PCT.doubleValue()))
            .andExpect(jsonPath("$.additionalFeesPct").value(DEFAULT_ADDITIONAL_FEES_PCT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFormula() throws Exception {
        // Get the formula
        restFormulaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formula
        Formula updatedFormula = formulaRepository.findById(formula.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormula are not directly saved in db
        em.detach(updatedFormula);
        updatedFormula.label(UPDATED_LABEL).adminFeesPct(UPDATED_ADMIN_FEES_PCT).additionalFeesPct(UPDATED_ADDITIONAL_FEES_PCT);

        restFormulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFormula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFormula))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFormulaToMatchAllProperties(updatedFormula);
    }

    @Test
    @Transactional
    void putNonExistingFormula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formula.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(put(ENTITY_API_URL_ID, formula.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formula)))
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormulaWithPatch() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formula using partial update
        Formula partialUpdatedFormula = new Formula();
        partialUpdatedFormula.setId(formula.getId());

        partialUpdatedFormula.label(UPDATED_LABEL);

        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormula))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormulaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFormula, formula), getPersistedFormula(formula));
    }

    @Test
    @Transactional
    void fullUpdateFormulaWithPatch() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formula using partial update
        Formula partialUpdatedFormula = new Formula();
        partialUpdatedFormula.setId(formula.getId());

        partialUpdatedFormula.label(UPDATED_LABEL).adminFeesPct(UPDATED_ADMIN_FEES_PCT).additionalFeesPct(UPDATED_ADDITIONAL_FEES_PCT);

        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormula.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormula))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormulaUpdatableFieldsEquals(partialUpdatedFormula, getPersistedFormula(partialUpdatedFormula));
    }

    @Test
    @Transactional
    void patchNonExistingFormula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formula.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formula.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(formula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormula() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formula.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(formula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formula in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the formula
        restFormulaMockMvc
            .perform(delete(ENTITY_API_URL_ID, formula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return formulaRepository.count();
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

    protected Formula getPersistedFormula(Formula formula) {
        return formulaRepository.findById(formula.getId()).orElseThrow();
    }

    protected void assertPersistedFormulaToMatchAllProperties(Formula expectedFormula) {
        assertFormulaAllPropertiesEquals(expectedFormula, getPersistedFormula(expectedFormula));
    }

    protected void assertPersistedFormulaToMatchUpdatableProperties(Formula expectedFormula) {
        assertFormulaAllUpdatablePropertiesEquals(expectedFormula, getPersistedFormula(expectedFormula));
    }
}
