package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.MileageAllowanceAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.MileageAllowance;
import io.simpleit.umbrella.repository.MileageAllowanceRepository;
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
 * Integration tests for the {@link MileageAllowanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MileageAllowanceResourceIT {

    private static final Double DEFAULT_MILEAGE = 1D;
    private static final Double UPDATED_MILEAGE = 2D;

    private static final Long DEFAULT_MULTIPLIER = 1L;
    private static final Long UPDATED_MULTIPLIER = 2L;

    private static final String ENTITY_API_URL = "/api/mileage-allowances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MileageAllowanceRepository mileageAllowanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMileageAllowanceMockMvc;

    private MileageAllowance mileageAllowance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MileageAllowance createEntity(EntityManager em) {
        MileageAllowance mileageAllowance = new MileageAllowance().mileage(DEFAULT_MILEAGE).multiplier(DEFAULT_MULTIPLIER);
        return mileageAllowance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MileageAllowance createUpdatedEntity(EntityManager em) {
        MileageAllowance mileageAllowance = new MileageAllowance().mileage(UPDATED_MILEAGE).multiplier(UPDATED_MULTIPLIER);
        return mileageAllowance;
    }

    @BeforeEach
    public void initTest() {
        mileageAllowance = createEntity(em);
    }

    @Test
    @Transactional
    void createMileageAllowance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MileageAllowance
        var returnedMileageAllowance = om.readValue(
            restMileageAllowanceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mileageAllowance)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MileageAllowance.class
        );

        // Validate the MileageAllowance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMileageAllowanceUpdatableFieldsEquals(returnedMileageAllowance, getPersistedMileageAllowance(returnedMileageAllowance));
    }

    @Test
    @Transactional
    void createMileageAllowanceWithExistingId() throws Exception {
        // Create the MileageAllowance with an existing ID
        mileageAllowance.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMileageAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mileageAllowance)))
            .andExpect(status().isBadRequest());

        // Validate the MileageAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMileageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mileageAllowance.setMileage(null);

        // Create the MileageAllowance, which fails.

        restMileageAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mileageAllowance)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMileageAllowances() throws Exception {
        // Initialize the database
        mileageAllowanceRepository.saveAndFlush(mileageAllowance);

        // Get all the mileageAllowanceList
        restMileageAllowanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mileageAllowance.getId().intValue())))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].multiplier").value(hasItem(DEFAULT_MULTIPLIER.intValue())));
    }

    @Test
    @Transactional
    void getMileageAllowance() throws Exception {
        // Initialize the database
        mileageAllowanceRepository.saveAndFlush(mileageAllowance);

        // Get the mileageAllowance
        restMileageAllowanceMockMvc
            .perform(get(ENTITY_API_URL_ID, mileageAllowance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mileageAllowance.getId().intValue()))
            .andExpect(jsonPath("$.mileage").value(DEFAULT_MILEAGE.doubleValue()))
            .andExpect(jsonPath("$.multiplier").value(DEFAULT_MULTIPLIER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMileageAllowance() throws Exception {
        // Get the mileageAllowance
        restMileageAllowanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMileageAllowance() throws Exception {
        // Initialize the database
        mileageAllowanceRepository.saveAndFlush(mileageAllowance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mileageAllowance
        MileageAllowance updatedMileageAllowance = mileageAllowanceRepository.findById(mileageAllowance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMileageAllowance are not directly saved in db
        em.detach(updatedMileageAllowance);
        updatedMileageAllowance.mileage(UPDATED_MILEAGE).multiplier(UPDATED_MULTIPLIER);

        restMileageAllowanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMileageAllowance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMileageAllowance))
            )
            .andExpect(status().isOk());

        // Validate the MileageAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMileageAllowanceToMatchAllProperties(updatedMileageAllowance);
    }

    @Test
    @Transactional
    void putNonExistingMileageAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mileageAllowance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMileageAllowanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mileageAllowance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mileageAllowance))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMileageAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mileageAllowance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageAllowanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mileageAllowance))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMileageAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mileageAllowance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageAllowanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mileageAllowance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MileageAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMileageAllowanceWithPatch() throws Exception {
        // Initialize the database
        mileageAllowanceRepository.saveAndFlush(mileageAllowance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mileageAllowance using partial update
        MileageAllowance partialUpdatedMileageAllowance = new MileageAllowance();
        partialUpdatedMileageAllowance.setId(mileageAllowance.getId());

        partialUpdatedMileageAllowance.multiplier(UPDATED_MULTIPLIER);

        restMileageAllowanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMileageAllowance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMileageAllowance))
            )
            .andExpect(status().isOk());

        // Validate the MileageAllowance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMileageAllowanceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMileageAllowance, mileageAllowance),
            getPersistedMileageAllowance(mileageAllowance)
        );
    }

    @Test
    @Transactional
    void fullUpdateMileageAllowanceWithPatch() throws Exception {
        // Initialize the database
        mileageAllowanceRepository.saveAndFlush(mileageAllowance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mileageAllowance using partial update
        MileageAllowance partialUpdatedMileageAllowance = new MileageAllowance();
        partialUpdatedMileageAllowance.setId(mileageAllowance.getId());

        partialUpdatedMileageAllowance.mileage(UPDATED_MILEAGE).multiplier(UPDATED_MULTIPLIER);

        restMileageAllowanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMileageAllowance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMileageAllowance))
            )
            .andExpect(status().isOk());

        // Validate the MileageAllowance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMileageAllowanceUpdatableFieldsEquals(
            partialUpdatedMileageAllowance,
            getPersistedMileageAllowance(partialUpdatedMileageAllowance)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMileageAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mileageAllowance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMileageAllowanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mileageAllowance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mileageAllowance))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMileageAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mileageAllowance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageAllowanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mileageAllowance))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMileageAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mileageAllowance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageAllowanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mileageAllowance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MileageAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMileageAllowance() throws Exception {
        // Initialize the database
        mileageAllowanceRepository.saveAndFlush(mileageAllowance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mileageAllowance
        restMileageAllowanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, mileageAllowance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mileageAllowanceRepository.count();
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

    protected MileageAllowance getPersistedMileageAllowance(MileageAllowance mileageAllowance) {
        return mileageAllowanceRepository.findById(mileageAllowance.getId()).orElseThrow();
    }

    protected void assertPersistedMileageAllowanceToMatchAllProperties(MileageAllowance expectedMileageAllowance) {
        assertMileageAllowanceAllPropertiesEquals(expectedMileageAllowance, getPersistedMileageAllowance(expectedMileageAllowance));
    }

    protected void assertPersistedMileageAllowanceToMatchUpdatableProperties(MileageAllowance expectedMileageAllowance) {
        assertMileageAllowanceAllUpdatablePropertiesEquals(
            expectedMileageAllowance,
            getPersistedMileageAllowance(expectedMileageAllowance)
        );
    }
}
