package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.ExpenseTypeAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.ExpenseType;
import io.simpleit.umbrella.repository.ExpenseTypeRepository;
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
 * Integration tests for the {@link ExpenseTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseTypeResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_REIMBURSMENT_PCT = 1D;
    private static final Double UPDATED_REIMBURSMENT_PCT = 2D;

    private static final String ENTITY_API_URL = "/api/expense-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExpenseTypeRepository expenseTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseTypeMockMvc;

    private ExpenseType expenseType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseType createEntity(EntityManager em) {
        ExpenseType expenseType = new ExpenseType()
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .reimbursmentPct(DEFAULT_REIMBURSMENT_PCT);
        return expenseType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseType createUpdatedEntity(EntityManager em) {
        ExpenseType expenseType = new ExpenseType()
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .reimbursmentPct(UPDATED_REIMBURSMENT_PCT);
        return expenseType;
    }

    @BeforeEach
    public void initTest() {
        expenseType = createEntity(em);
    }

    @Test
    @Transactional
    void createExpenseType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExpenseType
        var returnedExpenseType = om.readValue(
            restExpenseTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExpenseType.class
        );

        // Validate the ExpenseType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExpenseTypeUpdatableFieldsEquals(returnedExpenseType, getPersistedExpenseType(returnedExpenseType));
    }

    @Test
    @Transactional
    void createExpenseTypeWithExistingId() throws Exception {
        // Create the ExpenseType with an existing ID
        expenseType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseType)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExpenseTypes() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get all the expenseTypeList
        restExpenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].reimbursmentPct").value(hasItem(DEFAULT_REIMBURSMENT_PCT.doubleValue())));
    }

    @Test
    @Transactional
    void getExpenseType() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        // Get the expenseType
        restExpenseTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseType.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.reimbursmentPct").value(DEFAULT_REIMBURSMENT_PCT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingExpenseType() throws Exception {
        // Get the expenseType
        restExpenseTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenseType() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseType
        ExpenseType updatedExpenseType = expenseTypeRepository.findById(expenseType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExpenseType are not directly saved in db
        em.detach(updatedExpenseType);
        updatedExpenseType.label(UPDATED_LABEL).description(UPDATED_DESCRIPTION).reimbursmentPct(UPDATED_REIMBURSMENT_PCT);

        restExpenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpenseType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExpenseType))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExpenseTypeToMatchAllProperties(updatedExpenseType);
    }

    @Test
    @Transactional
    void putNonExistingExpenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpenseTypeWithPatch() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseType using partial update
        ExpenseType partialUpdatedExpenseType = new ExpenseType();
        partialUpdatedExpenseType.setId(expenseType.getId());

        partialUpdatedExpenseType.description(UPDATED_DESCRIPTION).reimbursmentPct(UPDATED_REIMBURSMENT_PCT);

        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseType))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExpenseType, expenseType),
            getPersistedExpenseType(expenseType)
        );
    }

    @Test
    @Transactional
    void fullUpdateExpenseTypeWithPatch() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseType using partial update
        ExpenseType partialUpdatedExpenseType = new ExpenseType();
        partialUpdatedExpenseType.setId(expenseType.getId());

        partialUpdatedExpenseType.label(UPDATED_LABEL).description(UPDATED_DESCRIPTION).reimbursmentPct(UPDATED_REIMBURSMENT_PCT);

        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseType))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseTypeUpdatableFieldsEquals(partialUpdatedExpenseType, getPersistedExpenseType(partialUpdatedExpenseType));
    }

    @Test
    @Transactional
    void patchNonExistingExpenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expenseType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpenseType() throws Exception {
        // Initialize the database
        expenseTypeRepository.saveAndFlush(expenseType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the expenseType
        restExpenseTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return expenseTypeRepository.count();
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

    protected ExpenseType getPersistedExpenseType(ExpenseType expenseType) {
        return expenseTypeRepository.findById(expenseType.getId()).orElseThrow();
    }

    protected void assertPersistedExpenseTypeToMatchAllProperties(ExpenseType expectedExpenseType) {
        assertExpenseTypeAllPropertiesEquals(expectedExpenseType, getPersistedExpenseType(expectedExpenseType));
    }

    protected void assertPersistedExpenseTypeToMatchUpdatableProperties(ExpenseType expectedExpenseType) {
        assertExpenseTypeAllUpdatablePropertiesEquals(expectedExpenseType, getPersistedExpenseType(expectedExpenseType));
    }
}
