package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.ProspectAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.Prospect;
import io.simpleit.umbrella.repository.ProspectRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ProspectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProspectResourceIT {

    private static final Long DEFAULT_DAILY_RATE = 1L;
    private static final Long UPDATED_DAILY_RATE = 2L;

    private static final Long DEFAULT_MONTHLY_EXPENSES = 1L;
    private static final Long UPDATED_MONTHLY_EXPENSES = 2L;

    private static final Double DEFAULT_TAX_RATE = 1D;
    private static final Double UPDATED_TAX_RATE = 2D;

    private static final LocalDate DEFAULT_EXPECTED_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPECTED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EXPECTED_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_EXPECTED_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prospects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProspectMockMvc;

    private Prospect prospect;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prospect createEntity(EntityManager em) {
        Prospect prospect = new Prospect()
            .dailyRate(DEFAULT_DAILY_RATE)
            .monthlyExpenses(DEFAULT_MONTHLY_EXPENSES)
            .taxRate(DEFAULT_TAX_RATE)
            .expectedStartDate(DEFAULT_EXPECTED_START_DATE)
            .expectedClient(DEFAULT_EXPECTED_CLIENT)
            .notes(DEFAULT_NOTES);
        return prospect;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prospect createUpdatedEntity(EntityManager em) {
        Prospect prospect = new Prospect()
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyExpenses(UPDATED_MONTHLY_EXPENSES)
            .taxRate(UPDATED_TAX_RATE)
            .expectedStartDate(UPDATED_EXPECTED_START_DATE)
            .expectedClient(UPDATED_EXPECTED_CLIENT)
            .notes(UPDATED_NOTES);
        return prospect;
    }

    @BeforeEach
    public void initTest() {
        prospect = createEntity(em);
    }

    @Test
    @Transactional
    void createProspect() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Prospect
        var returnedProspect = om.readValue(
            restProspectMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospect)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Prospect.class
        );

        // Validate the Prospect in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProspectUpdatableFieldsEquals(returnedProspect, getPersistedProspect(returnedProspect));
    }

    @Test
    @Transactional
    void createProspectWithExistingId() throws Exception {
        // Create the Prospect with an existing ID
        prospect.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProspectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospect)))
            .andExpect(status().isBadRequest());

        // Validate the Prospect in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProspects() throws Exception {
        // Initialize the database
        prospectRepository.saveAndFlush(prospect);

        // Get all the prospectList
        restProspectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prospect.getId().intValue())))
            .andExpect(jsonPath("$.[*].dailyRate").value(hasItem(DEFAULT_DAILY_RATE.intValue())))
            .andExpect(jsonPath("$.[*].monthlyExpenses").value(hasItem(DEFAULT_MONTHLY_EXPENSES.intValue())))
            .andExpect(jsonPath("$.[*].taxRate").value(hasItem(DEFAULT_TAX_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].expectedStartDate").value(hasItem(DEFAULT_EXPECTED_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectedClient").value(hasItem(DEFAULT_EXPECTED_CLIENT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getProspect() throws Exception {
        // Initialize the database
        prospectRepository.saveAndFlush(prospect);

        // Get the prospect
        restProspectMockMvc
            .perform(get(ENTITY_API_URL_ID, prospect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prospect.getId().intValue()))
            .andExpect(jsonPath("$.dailyRate").value(DEFAULT_DAILY_RATE.intValue()))
            .andExpect(jsonPath("$.monthlyExpenses").value(DEFAULT_MONTHLY_EXPENSES.intValue()))
            .andExpect(jsonPath("$.taxRate").value(DEFAULT_TAX_RATE.doubleValue()))
            .andExpect(jsonPath("$.expectedStartDate").value(DEFAULT_EXPECTED_START_DATE.toString()))
            .andExpect(jsonPath("$.expectedClient").value(DEFAULT_EXPECTED_CLIENT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingProspect() throws Exception {
        // Get the prospect
        restProspectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProspect() throws Exception {
        // Initialize the database
        prospectRepository.saveAndFlush(prospect);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospect
        Prospect updatedProspect = prospectRepository.findById(prospect.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProspect are not directly saved in db
        em.detach(updatedProspect);
        updatedProspect
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyExpenses(UPDATED_MONTHLY_EXPENSES)
            .taxRate(UPDATED_TAX_RATE)
            .expectedStartDate(UPDATED_EXPECTED_START_DATE)
            .expectedClient(UPDATED_EXPECTED_CLIENT)
            .notes(UPDATED_NOTES);

        restProspectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProspect.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProspect))
            )
            .andExpect(status().isOk());

        // Validate the Prospect in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProspectToMatchAllProperties(updatedProspect);
    }

    @Test
    @Transactional
    void putNonExistingProspect() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospect.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProspectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prospect.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospect))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prospect in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProspect() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospect.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(prospect))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prospect in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProspect() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospect.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(prospect)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prospect in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProspectWithPatch() throws Exception {
        // Initialize the database
        prospectRepository.saveAndFlush(prospect);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospect using partial update
        Prospect partialUpdatedProspect = new Prospect();
        partialUpdatedProspect.setId(prospect.getId());

        partialUpdatedProspect
            .dailyRate(UPDATED_DAILY_RATE)
            .taxRate(UPDATED_TAX_RATE)
            .expectedClient(UPDATED_EXPECTED_CLIENT)
            .notes(UPDATED_NOTES);

        restProspectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProspect.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProspect))
            )
            .andExpect(status().isOk());

        // Validate the Prospect in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProspectUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProspect, prospect), getPersistedProspect(prospect));
    }

    @Test
    @Transactional
    void fullUpdateProspectWithPatch() throws Exception {
        // Initialize the database
        prospectRepository.saveAndFlush(prospect);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the prospect using partial update
        Prospect partialUpdatedProspect = new Prospect();
        partialUpdatedProspect.setId(prospect.getId());

        partialUpdatedProspect
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyExpenses(UPDATED_MONTHLY_EXPENSES)
            .taxRate(UPDATED_TAX_RATE)
            .expectedStartDate(UPDATED_EXPECTED_START_DATE)
            .expectedClient(UPDATED_EXPECTED_CLIENT)
            .notes(UPDATED_NOTES);

        restProspectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProspect.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProspect))
            )
            .andExpect(status().isOk());

        // Validate the Prospect in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProspectUpdatableFieldsEquals(partialUpdatedProspect, getPersistedProspect(partialUpdatedProspect));
    }

    @Test
    @Transactional
    void patchNonExistingProspect() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospect.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProspectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prospect.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prospect))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prospect in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProspect() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospect.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(prospect))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prospect in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProspect() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        prospect.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProspectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(prospect)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prospect in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProspect() throws Exception {
        // Initialize the database
        prospectRepository.saveAndFlush(prospect);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the prospect
        restProspectMockMvc
            .perform(delete(ENTITY_API_URL_ID, prospect.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return prospectRepository.count();
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

    protected Prospect getPersistedProspect(Prospect prospect) {
        return prospectRepository.findById(prospect.getId()).orElseThrow();
    }

    protected void assertPersistedProspectToMatchAllProperties(Prospect expectedProspect) {
        assertProspectAllPropertiesEquals(expectedProspect, getPersistedProspect(expectedProspect));
    }

    protected void assertPersistedProspectToMatchUpdatableProperties(Prospect expectedProspect) {
        assertProspectAllUpdatablePropertiesEquals(expectedProspect, getPersistedProspect(expectedProspect));
    }
}
