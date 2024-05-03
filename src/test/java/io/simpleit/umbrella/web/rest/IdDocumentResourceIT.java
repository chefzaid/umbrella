package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.IdDocumentAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.IdDocument;
import io.simpleit.umbrella.domain.enumeration.IdType;
import io.simpleit.umbrella.repository.IdDocumentRepository;
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
 * Integration tests for the {@link IdDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IdDocumentResourceIT {

    private static final IdType DEFAULT_ID_TYPE = IdType.PASSPORT;
    private static final IdType UPDATED_ID_TYPE = IdType.NATIONAL_ID;

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/id-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IdDocumentRepository idDocumentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdDocumentMockMvc;

    private IdDocument idDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdDocument createEntity(EntityManager em) {
        IdDocument idDocument = new IdDocument().idType(DEFAULT_ID_TYPE).idNumber(DEFAULT_ID_NUMBER);
        return idDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdDocument createUpdatedEntity(EntityManager em) {
        IdDocument idDocument = new IdDocument().idType(UPDATED_ID_TYPE).idNumber(UPDATED_ID_NUMBER);
        return idDocument;
    }

    @BeforeEach
    public void initTest() {
        idDocument = createEntity(em);
    }

    @Test
    @Transactional
    void createIdDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IdDocument
        var returnedIdDocument = om.readValue(
            restIdDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idDocument)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IdDocument.class
        );

        // Validate the IdDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertIdDocumentUpdatableFieldsEquals(returnedIdDocument, getPersistedIdDocument(returnedIdDocument));
    }

    @Test
    @Transactional
    void createIdDocumentWithExistingId() throws Exception {
        // Create the IdDocument with an existing ID
        idDocument.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idDocument)))
            .andExpect(status().isBadRequest());

        // Validate the IdDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        idDocument.setIdType(null);

        // Create the IdDocument, which fails.

        restIdDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idDocument)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        idDocument.setIdNumber(null);

        // Create the IdDocument, which fails.

        restIdDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idDocument)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdDocuments() throws Exception {
        // Initialize the database
        idDocumentRepository.saveAndFlush(idDocument);

        // Get all the idDocumentList
        restIdDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].idType").value(hasItem(DEFAULT_ID_TYPE.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)));
    }

    @Test
    @Transactional
    void getIdDocument() throws Exception {
        // Initialize the database
        idDocumentRepository.saveAndFlush(idDocument);

        // Get the idDocument
        restIdDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, idDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(idDocument.getId().intValue()))
            .andExpect(jsonPath("$.idType").value(DEFAULT_ID_TYPE.toString()))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingIdDocument() throws Exception {
        // Get the idDocument
        restIdDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIdDocument() throws Exception {
        // Initialize the database
        idDocumentRepository.saveAndFlush(idDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idDocument
        IdDocument updatedIdDocument = idDocumentRepository.findById(idDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIdDocument are not directly saved in db
        em.detach(updatedIdDocument);
        updatedIdDocument.idType(UPDATED_ID_TYPE).idNumber(UPDATED_ID_NUMBER);

        restIdDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIdDocument.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedIdDocument))
            )
            .andExpect(status().isOk());

        // Validate the IdDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIdDocumentToMatchAllProperties(updatedIdDocument);
    }

    @Test
    @Transactional
    void putNonExistingIdDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idDocument.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, idDocument.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idDocument.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(idDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idDocument.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(idDocument)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdDocumentWithPatch() throws Exception {
        // Initialize the database
        idDocumentRepository.saveAndFlush(idDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idDocument using partial update
        IdDocument partialUpdatedIdDocument = new IdDocument();
        partialUpdatedIdDocument.setId(idDocument.getId());

        partialUpdatedIdDocument.idNumber(UPDATED_ID_NUMBER);

        restIdDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdDocument))
            )
            .andExpect(status().isOk());

        // Validate the IdDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIdDocument, idDocument),
            getPersistedIdDocument(idDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateIdDocumentWithPatch() throws Exception {
        // Initialize the database
        idDocumentRepository.saveAndFlush(idDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the idDocument using partial update
        IdDocument partialUpdatedIdDocument = new IdDocument();
        partialUpdatedIdDocument.setId(idDocument.getId());

        partialUpdatedIdDocument.idType(UPDATED_ID_TYPE).idNumber(UPDATED_ID_NUMBER);

        restIdDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdDocument))
            )
            .andExpect(status().isOk());

        // Validate the IdDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdDocumentUpdatableFieldsEquals(partialUpdatedIdDocument, getPersistedIdDocument(partialUpdatedIdDocument));
    }

    @Test
    @Transactional
    void patchNonExistingIdDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idDocument.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, idDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(idDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idDocument.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(idDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        idDocument.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(idDocument)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdDocument() throws Exception {
        // Initialize the database
        idDocumentRepository.saveAndFlush(idDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the idDocument
        restIdDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, idDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return idDocumentRepository.count();
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

    protected IdDocument getPersistedIdDocument(IdDocument idDocument) {
        return idDocumentRepository.findById(idDocument.getId()).orElseThrow();
    }

    protected void assertPersistedIdDocumentToMatchAllProperties(IdDocument expectedIdDocument) {
        assertIdDocumentAllPropertiesEquals(expectedIdDocument, getPersistedIdDocument(expectedIdDocument));
    }

    protected void assertPersistedIdDocumentToMatchUpdatableProperties(IdDocument expectedIdDocument) {
        assertIdDocumentAllUpdatablePropertiesEquals(expectedIdDocument, getPersistedIdDocument(expectedIdDocument));
    }
}
