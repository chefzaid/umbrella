package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.TimeSheetAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.TimeSheet;
import io.simpleit.umbrella.repository.TimeSheetRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TimeSheetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeSheetResourceIT {

    private static final String DEFAULT_CONCERNED_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_CONCERNED_MONTH = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_SUBMIT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMIT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALIDATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/time-sheets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeSheetMockMvc;

    private TimeSheet timeSheet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSheet createEntity(EntityManager em) {
        TimeSheet timeSheet = new TimeSheet()
            .concernedMonth(DEFAULT_CONCERNED_MONTH)
            .creationDate(DEFAULT_CREATION_DATE)
            .submitDate(DEFAULT_SUBMIT_DATE)
            .validationDate(DEFAULT_VALIDATION_DATE);
        return timeSheet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSheet createUpdatedEntity(EntityManager em) {
        TimeSheet timeSheet = new TimeSheet()
            .concernedMonth(UPDATED_CONCERNED_MONTH)
            .creationDate(UPDATED_CREATION_DATE)
            .submitDate(UPDATED_SUBMIT_DATE)
            .validationDate(UPDATED_VALIDATION_DATE);
        return timeSheet;
    }

    @BeforeEach
    public void initTest() {
        timeSheet = createEntity(em);
    }

    @Test
    @Transactional
    void createTimeSheet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TimeSheet
        var returnedTimeSheet = om.readValue(
            restTimeSheetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimeSheet.class
        );

        // Validate the TimeSheet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTimeSheetUpdatableFieldsEquals(returnedTimeSheet, getPersistedTimeSheet(returnedTimeSheet));
    }

    @Test
    @Transactional
    void createTimeSheetWithExistingId() throws Exception {
        // Create the TimeSheet with an existing ID
        timeSheet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheet)))
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConcernedMonthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timeSheet.setConcernedMonth(null);

        // Create the TimeSheet, which fails.

        restTimeSheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheet)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimeSheets() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        // Get all the timeSheetList
        restTimeSheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeSheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].concernedMonth").value(hasItem(DEFAULT_CONCERNED_MONTH)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].submitDate").value(hasItem(DEFAULT_SUBMIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].validationDate").value(hasItem(DEFAULT_VALIDATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        // Get the timeSheet
        restTimeSheetMockMvc
            .perform(get(ENTITY_API_URL_ID, timeSheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeSheet.getId().intValue()))
            .andExpect(jsonPath("$.concernedMonth").value(DEFAULT_CONCERNED_MONTH))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.submitDate").value(DEFAULT_SUBMIT_DATE.toString()))
            .andExpect(jsonPath("$.validationDate").value(DEFAULT_VALIDATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTimeSheet() throws Exception {
        // Get the timeSheet
        restTimeSheetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSheet
        TimeSheet updatedTimeSheet = timeSheetRepository.findById(timeSheet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimeSheet are not directly saved in db
        em.detach(updatedTimeSheet);
        updatedTimeSheet
            .concernedMonth(UPDATED_CONCERNED_MONTH)
            .creationDate(UPDATED_CREATION_DATE)
            .submitDate(UPDATED_SUBMIT_DATE)
            .validationDate(UPDATED_VALIDATION_DATE);

        restTimeSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTimeSheet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTimeSheet))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimeSheetToMatchAllProperties(updatedTimeSheet);
    }

    @Test
    @Transactional
    void putNonExistingTimeSheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSheet.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheet))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeSheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeSheet))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeSheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeSheetWithPatch() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSheet using partial update
        TimeSheet partialUpdatedTimeSheet = new TimeSheet();
        partialUpdatedTimeSheet.setId(timeSheet.getId());

        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeSheet))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeSheetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimeSheet, timeSheet),
            getPersistedTimeSheet(timeSheet)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimeSheetWithPatch() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSheet using partial update
        TimeSheet partialUpdatedTimeSheet = new TimeSheet();
        partialUpdatedTimeSheet.setId(timeSheet.getId());

        partialUpdatedTimeSheet
            .concernedMonth(UPDATED_CONCERNED_MONTH)
            .creationDate(UPDATED_CREATION_DATE)
            .submitDate(UPDATED_SUBMIT_DATE)
            .validationDate(UPDATED_VALIDATION_DATE);

        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeSheet))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeSheetUpdatableFieldsEquals(partialUpdatedTimeSheet, getPersistedTimeSheet(partialUpdatedTimeSheet));
    }

    @Test
    @Transactional
    void patchNonExistingTimeSheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeSheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeSheet))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeSheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeSheet))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeSheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timeSheet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeSheet() throws Exception {
        // Initialize the database
        timeSheetRepository.saveAndFlush(timeSheet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timeSheet
        restTimeSheetMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeSheet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timeSheetRepository.count();
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

    protected TimeSheet getPersistedTimeSheet(TimeSheet timeSheet) {
        return timeSheetRepository.findById(timeSheet.getId()).orElseThrow();
    }

    protected void assertPersistedTimeSheetToMatchAllProperties(TimeSheet expectedTimeSheet) {
        assertTimeSheetAllPropertiesEquals(expectedTimeSheet, getPersistedTimeSheet(expectedTimeSheet));
    }

    protected void assertPersistedTimeSheetToMatchUpdatableProperties(TimeSheet expectedTimeSheet) {
        assertTimeSheetAllUpdatablePropertiesEquals(expectedTimeSheet, getPersistedTimeSheet(expectedTimeSheet));
    }
}
