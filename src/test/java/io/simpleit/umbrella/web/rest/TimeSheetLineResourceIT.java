package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.TimeSheetLineAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.TimeSheetLine;
import io.simpleit.umbrella.repository.TimeSheetLineRepository;
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
 * Integration tests for the {@link TimeSheetLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeSheetLineResourceIT {

    private static final Double DEFAULT_MONTHLY_DAYS = 1D;
    private static final Double UPDATED_MONTHLY_DAYS = 2D;

    private static final Double DEFAULT_EXTRA_HOURS = 1D;
    private static final Double UPDATED_EXTRA_HOURS = 2D;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/time-sheet-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimeSheetLineRepository timeSheetLineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeSheetLineMockMvc;

    private TimeSheetLine timeSheetLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSheetLine createEntity(EntityManager em) {
        TimeSheetLine timeSheetLine = new TimeSheetLine()
            .monthlyDays(DEFAULT_MONTHLY_DAYS)
            .extraHours(DEFAULT_EXTRA_HOURS)
            .comments(DEFAULT_COMMENTS);
        return timeSheetLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeSheetLine createUpdatedEntity(EntityManager em) {
        TimeSheetLine timeSheetLine = new TimeSheetLine()
            .monthlyDays(UPDATED_MONTHLY_DAYS)
            .extraHours(UPDATED_EXTRA_HOURS)
            .comments(UPDATED_COMMENTS);
        return timeSheetLine;
    }

    @BeforeEach
    public void initTest() {
        timeSheetLine = createEntity(em);
    }

    @Test
    @Transactional
    void createTimeSheetLine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TimeSheetLine
        var returnedTimeSheetLine = om.readValue(
            restTimeSheetLineMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheetLine)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimeSheetLine.class
        );

        // Validate the TimeSheetLine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTimeSheetLineUpdatableFieldsEquals(returnedTimeSheetLine, getPersistedTimeSheetLine(returnedTimeSheetLine));
    }

    @Test
    @Transactional
    void createTimeSheetLineWithExistingId() throws Exception {
        // Create the TimeSheetLine with an existing ID
        timeSheetLine.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeSheetLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheetLine)))
            .andExpect(status().isBadRequest());

        // Validate the TimeSheetLine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMonthlyDaysIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timeSheetLine.setMonthlyDays(null);

        // Create the TimeSheetLine, which fails.

        restTimeSheetLineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheetLine)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimeSheetLines() throws Exception {
        // Initialize the database
        timeSheetLineRepository.saveAndFlush(timeSheetLine);

        // Get all the timeSheetLineList
        restTimeSheetLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeSheetLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthlyDays").value(hasItem(DEFAULT_MONTHLY_DAYS.doubleValue())))
            .andExpect(jsonPath("$.[*].extraHours").value(hasItem(DEFAULT_EXTRA_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getTimeSheetLine() throws Exception {
        // Initialize the database
        timeSheetLineRepository.saveAndFlush(timeSheetLine);

        // Get the timeSheetLine
        restTimeSheetLineMockMvc
            .perform(get(ENTITY_API_URL_ID, timeSheetLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeSheetLine.getId().intValue()))
            .andExpect(jsonPath("$.monthlyDays").value(DEFAULT_MONTHLY_DAYS.doubleValue()))
            .andExpect(jsonPath("$.extraHours").value(DEFAULT_EXTRA_HOURS.doubleValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingTimeSheetLine() throws Exception {
        // Get the timeSheetLine
        restTimeSheetLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimeSheetLine() throws Exception {
        // Initialize the database
        timeSheetLineRepository.saveAndFlush(timeSheetLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSheetLine
        TimeSheetLine updatedTimeSheetLine = timeSheetLineRepository.findById(timeSheetLine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimeSheetLine are not directly saved in db
        em.detach(updatedTimeSheetLine);
        updatedTimeSheetLine.monthlyDays(UPDATED_MONTHLY_DAYS).extraHours(UPDATED_EXTRA_HOURS).comments(UPDATED_COMMENTS);

        restTimeSheetLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTimeSheetLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTimeSheetLine))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheetLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimeSheetLineToMatchAllProperties(updatedTimeSheetLine);
    }

    @Test
    @Transactional
    void putNonExistingTimeSheetLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheetLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSheetLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeSheetLine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeSheetLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheetLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeSheetLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheetLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeSheetLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheetLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeSheetLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheetLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeSheetLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSheetLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeSheetLineWithPatch() throws Exception {
        // Initialize the database
        timeSheetLineRepository.saveAndFlush(timeSheetLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSheetLine using partial update
        TimeSheetLine partialUpdatedTimeSheetLine = new TimeSheetLine();
        partialUpdatedTimeSheetLine.setId(timeSheetLine.getId());

        partialUpdatedTimeSheetLine.monthlyDays(UPDATED_MONTHLY_DAYS).extraHours(UPDATED_EXTRA_HOURS);

        restTimeSheetLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSheetLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeSheetLine))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheetLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeSheetLineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimeSheetLine, timeSheetLine),
            getPersistedTimeSheetLine(timeSheetLine)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimeSheetLineWithPatch() throws Exception {
        // Initialize the database
        timeSheetLineRepository.saveAndFlush(timeSheetLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeSheetLine using partial update
        TimeSheetLine partialUpdatedTimeSheetLine = new TimeSheetLine();
        partialUpdatedTimeSheetLine.setId(timeSheetLine.getId());

        partialUpdatedTimeSheetLine.monthlyDays(UPDATED_MONTHLY_DAYS).extraHours(UPDATED_EXTRA_HOURS).comments(UPDATED_COMMENTS);

        restTimeSheetLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeSheetLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeSheetLine))
            )
            .andExpect(status().isOk());

        // Validate the TimeSheetLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeSheetLineUpdatableFieldsEquals(partialUpdatedTimeSheetLine, getPersistedTimeSheetLine(partialUpdatedTimeSheetLine));
    }

    @Test
    @Transactional
    void patchNonExistingTimeSheetLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheetLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeSheetLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeSheetLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeSheetLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheetLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeSheetLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheetLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeSheetLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeSheetLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeSheetLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeSheetLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeSheetLineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timeSheetLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeSheetLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeSheetLine() throws Exception {
        // Initialize the database
        timeSheetLineRepository.saveAndFlush(timeSheetLine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timeSheetLine
        restTimeSheetLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeSheetLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timeSheetLineRepository.count();
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

    protected TimeSheetLine getPersistedTimeSheetLine(TimeSheetLine timeSheetLine) {
        return timeSheetLineRepository.findById(timeSheetLine.getId()).orElseThrow();
    }

    protected void assertPersistedTimeSheetLineToMatchAllProperties(TimeSheetLine expectedTimeSheetLine) {
        assertTimeSheetLineAllPropertiesEquals(expectedTimeSheetLine, getPersistedTimeSheetLine(expectedTimeSheetLine));
    }

    protected void assertPersistedTimeSheetLineToMatchUpdatableProperties(TimeSheetLine expectedTimeSheetLine) {
        assertTimeSheetLineAllUpdatablePropertiesEquals(expectedTimeSheetLine, getPersistedTimeSheetLine(expectedTimeSheetLine));
    }
}
