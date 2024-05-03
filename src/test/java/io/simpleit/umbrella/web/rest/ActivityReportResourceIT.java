package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.ActivityReportAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.ActivityReport;
import io.simpleit.umbrella.repository.ActivityReportRepository;
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
 * Integration tests for the {@link ActivityReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivityReportResourceIT {

    private static final LocalDate DEFAULT_MONTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MONTH = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/activity-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivityReportRepository activityReportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivityReportMockMvc;

    private ActivityReport activityReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityReport createEntity(EntityManager em) {
        ActivityReport activityReport = new ActivityReport().month(DEFAULT_MONTH).balance(DEFAULT_BALANCE).comments(DEFAULT_COMMENTS);
        return activityReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityReport createUpdatedEntity(EntityManager em) {
        ActivityReport activityReport = new ActivityReport().month(UPDATED_MONTH).balance(UPDATED_BALANCE).comments(UPDATED_COMMENTS);
        return activityReport;
    }

    @BeforeEach
    public void initTest() {
        activityReport = createEntity(em);
    }

    @Test
    @Transactional
    void createActivityReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ActivityReport
        var returnedActivityReport = om.readValue(
            restActivityReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityReport)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ActivityReport.class
        );

        // Validate the ActivityReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertActivityReportUpdatableFieldsEquals(returnedActivityReport, getPersistedActivityReport(returnedActivityReport));
    }

    @Test
    @Transactional
    void createActivityReportWithExistingId() throws Exception {
        // Create the ActivityReport with an existing ID
        activityReport.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityReport)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActivityReports() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);

        // Get all the activityReportList
        restActivityReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getActivityReport() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);

        // Get the activityReport
        restActivityReportMockMvc
            .perform(get(ENTITY_API_URL_ID, activityReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activityReport.getId().intValue()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingActivityReport() throws Exception {
        // Get the activityReport
        restActivityReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivityReport() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityReport
        ActivityReport updatedActivityReport = activityReportRepository.findById(activityReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivityReport are not directly saved in db
        em.detach(updatedActivityReport);
        updatedActivityReport.month(UPDATED_MONTH).balance(UPDATED_BALANCE).comments(UPDATED_COMMENTS);

        restActivityReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivityReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedActivityReport))
            )
            .andExpect(status().isOk());

        // Validate the ActivityReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActivityReportToMatchAllProperties(updatedActivityReport);
    }

    @Test
    @Transactional
    void putNonExistingActivityReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activityReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivityReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activityReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivityReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activityReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActivityReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivityReportWithPatch() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityReport using partial update
        ActivityReport partialUpdatedActivityReport = new ActivityReport();
        partialUpdatedActivityReport.setId(activityReport.getId());

        partialUpdatedActivityReport.month(UPDATED_MONTH).balance(UPDATED_BALANCE).comments(UPDATED_COMMENTS);

        restActivityReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivityReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivityReport))
            )
            .andExpect(status().isOk());

        // Validate the ActivityReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedActivityReport, activityReport),
            getPersistedActivityReport(activityReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateActivityReportWithPatch() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activityReport using partial update
        ActivityReport partialUpdatedActivityReport = new ActivityReport();
        partialUpdatedActivityReport.setId(activityReport.getId());

        partialUpdatedActivityReport.month(UPDATED_MONTH).balance(UPDATED_BALANCE).comments(UPDATED_COMMENTS);

        restActivityReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivityReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivityReport))
            )
            .andExpect(status().isOk());

        // Validate the ActivityReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivityReportUpdatableFieldsEquals(partialUpdatedActivityReport, getPersistedActivityReport(partialUpdatedActivityReport));
    }

    @Test
    @Transactional
    void patchNonExistingActivityReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activityReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivityReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activityReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivityReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivityReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activityReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivityReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activityReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActivityReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivityReport() throws Exception {
        // Initialize the database
        activityReportRepository.saveAndFlush(activityReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activityReport
        restActivityReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, activityReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activityReportRepository.count();
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

    protected ActivityReport getPersistedActivityReport(ActivityReport activityReport) {
        return activityReportRepository.findById(activityReport.getId()).orElseThrow();
    }

    protected void assertPersistedActivityReportToMatchAllProperties(ActivityReport expectedActivityReport) {
        assertActivityReportAllPropertiesEquals(expectedActivityReport, getPersistedActivityReport(expectedActivityReport));
    }

    protected void assertPersistedActivityReportToMatchUpdatableProperties(ActivityReport expectedActivityReport) {
        assertActivityReportAllUpdatablePropertiesEquals(expectedActivityReport, getPersistedActivityReport(expectedActivityReport));
    }
}
