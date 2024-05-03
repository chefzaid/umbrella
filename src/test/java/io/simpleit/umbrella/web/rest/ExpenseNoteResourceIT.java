package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.ExpenseNoteAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.ExpenseNote;
import io.simpleit.umbrella.repository.ExpenseNoteRepository;
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
 * Integration tests for the {@link ExpenseNoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseNoteResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_CONCERNED_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_CONCERNED_MONTH = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_SUBMIT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMIT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALIDATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/expense-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExpenseNoteRepository expenseNoteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseNoteMockMvc;

    private ExpenseNote expenseNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseNote createEntity(EntityManager em) {
        ExpenseNote expenseNote = new ExpenseNote()
            .label(DEFAULT_LABEL)
            .concernedMonth(DEFAULT_CONCERNED_MONTH)
            .creationDate(DEFAULT_CREATION_DATE)
            .submitDate(DEFAULT_SUBMIT_DATE)
            .validationDate(DEFAULT_VALIDATION_DATE);
        return expenseNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseNote createUpdatedEntity(EntityManager em) {
        ExpenseNote expenseNote = new ExpenseNote()
            .label(UPDATED_LABEL)
            .concernedMonth(UPDATED_CONCERNED_MONTH)
            .creationDate(UPDATED_CREATION_DATE)
            .submitDate(UPDATED_SUBMIT_DATE)
            .validationDate(UPDATED_VALIDATION_DATE);
        return expenseNote;
    }

    @BeforeEach
    public void initTest() {
        expenseNote = createEntity(em);
    }

    @Test
    @Transactional
    void createExpenseNote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExpenseNote
        var returnedExpenseNote = om.readValue(
            restExpenseNoteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseNote)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExpenseNote.class
        );

        // Validate the ExpenseNote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExpenseNoteUpdatableFieldsEquals(returnedExpenseNote, getPersistedExpenseNote(returnedExpenseNote));
    }

    @Test
    @Transactional
    void createExpenseNoteWithExistingId() throws Exception {
        // Create the ExpenseNote with an existing ID
        expenseNote.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseNote)))
            .andExpect(status().isBadRequest());

        // Validate the ExpenseNote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExpenseNotes() throws Exception {
        // Initialize the database
        expenseNoteRepository.saveAndFlush(expenseNote);

        // Get all the expenseNoteList
        restExpenseNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expenseNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].concernedMonth").value(hasItem(DEFAULT_CONCERNED_MONTH)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].submitDate").value(hasItem(DEFAULT_SUBMIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].validationDate").value(hasItem(DEFAULT_VALIDATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getExpenseNote() throws Exception {
        // Initialize the database
        expenseNoteRepository.saveAndFlush(expenseNote);

        // Get the expenseNote
        restExpenseNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, expenseNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expenseNote.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.concernedMonth").value(DEFAULT_CONCERNED_MONTH))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.submitDate").value(DEFAULT_SUBMIT_DATE.toString()))
            .andExpect(jsonPath("$.validationDate").value(DEFAULT_VALIDATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExpenseNote() throws Exception {
        // Get the expenseNote
        restExpenseNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpenseNote() throws Exception {
        // Initialize the database
        expenseNoteRepository.saveAndFlush(expenseNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseNote
        ExpenseNote updatedExpenseNote = expenseNoteRepository.findById(expenseNote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExpenseNote are not directly saved in db
        em.detach(updatedExpenseNote);
        updatedExpenseNote
            .label(UPDATED_LABEL)
            .concernedMonth(UPDATED_CONCERNED_MONTH)
            .creationDate(UPDATED_CREATION_DATE)
            .submitDate(UPDATED_SUBMIT_DATE)
            .validationDate(UPDATED_VALIDATION_DATE);

        restExpenseNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpenseNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExpenseNote))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExpenseNoteToMatchAllProperties(updatedExpenseNote);
    }

    @Test
    @Transactional
    void putNonExistingExpenseNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseNote.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expenseNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpenseNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseNote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expenseNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpenseNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseNote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseNoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expenseNote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpenseNoteWithPatch() throws Exception {
        // Initialize the database
        expenseNoteRepository.saveAndFlush(expenseNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseNote using partial update
        ExpenseNote partialUpdatedExpenseNote = new ExpenseNote();
        partialUpdatedExpenseNote.setId(expenseNote.getId());

        partialUpdatedExpenseNote.label(UPDATED_LABEL).validationDate(UPDATED_VALIDATION_DATE);

        restExpenseNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseNote))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseNote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseNoteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExpenseNote, expenseNote),
            getPersistedExpenseNote(expenseNote)
        );
    }

    @Test
    @Transactional
    void fullUpdateExpenseNoteWithPatch() throws Exception {
        // Initialize the database
        expenseNoteRepository.saveAndFlush(expenseNote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expenseNote using partial update
        ExpenseNote partialUpdatedExpenseNote = new ExpenseNote();
        partialUpdatedExpenseNote.setId(expenseNote.getId());

        partialUpdatedExpenseNote
            .label(UPDATED_LABEL)
            .concernedMonth(UPDATED_CONCERNED_MONTH)
            .creationDate(UPDATED_CREATION_DATE)
            .submitDate(UPDATED_SUBMIT_DATE)
            .validationDate(UPDATED_VALIDATION_DATE);

        restExpenseNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpenseNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpenseNote))
            )
            .andExpect(status().isOk());

        // Validate the ExpenseNote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseNoteUpdatableFieldsEquals(partialUpdatedExpenseNote, getPersistedExpenseNote(partialUpdatedExpenseNote));
    }

    @Test
    @Transactional
    void patchNonExistingExpenseNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseNote.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expenseNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpenseNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseNote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expenseNote))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExpenseNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpenseNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expenseNote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseNoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expenseNote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExpenseNote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpenseNote() throws Exception {
        // Initialize the database
        expenseNoteRepository.saveAndFlush(expenseNote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the expenseNote
        restExpenseNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, expenseNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return expenseNoteRepository.count();
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

    protected ExpenseNote getPersistedExpenseNote(ExpenseNote expenseNote) {
        return expenseNoteRepository.findById(expenseNote.getId()).orElseThrow();
    }

    protected void assertPersistedExpenseNoteToMatchAllProperties(ExpenseNote expectedExpenseNote) {
        assertExpenseNoteAllPropertiesEquals(expectedExpenseNote, getPersistedExpenseNote(expectedExpenseNote));
    }

    protected void assertPersistedExpenseNoteToMatchUpdatableProperties(ExpenseNote expectedExpenseNote) {
        assertExpenseNoteAllUpdatablePropertiesEquals(expectedExpenseNote, getPersistedExpenseNote(expectedExpenseNote));
    }
}
