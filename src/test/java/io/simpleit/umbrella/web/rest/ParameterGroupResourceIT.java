package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.ParameterGroupAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.ParameterGroup;
import io.simpleit.umbrella.repository.ParameterGroupRepository;
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
 * Integration tests for the {@link ParameterGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParameterGroupResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parameter-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ParameterGroupRepository parameterGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParameterGroupMockMvc;

    private ParameterGroup parameterGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterGroup createEntity(EntityManager em) {
        ParameterGroup parameterGroup = new ParameterGroup().label(DEFAULT_LABEL).description(DEFAULT_DESCRIPTION);
        return parameterGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterGroup createUpdatedEntity(EntityManager em) {
        ParameterGroup parameterGroup = new ParameterGroup().label(UPDATED_LABEL).description(UPDATED_DESCRIPTION);
        return parameterGroup;
    }

    @BeforeEach
    public void initTest() {
        parameterGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createParameterGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ParameterGroup
        var returnedParameterGroup = om.readValue(
            restParameterGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameterGroup)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ParameterGroup.class
        );

        // Validate the ParameterGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertParameterGroupUpdatableFieldsEquals(returnedParameterGroup, getPersistedParameterGroup(returnedParameterGroup));
    }

    @Test
    @Transactional
    void createParameterGroupWithExistingId() throws Exception {
        // Create the ParameterGroup with an existing ID
        parameterGroup.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameterGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ParameterGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParameterGroups() throws Exception {
        // Initialize the database
        parameterGroupRepository.saveAndFlush(parameterGroup);

        // Get all the parameterGroupList
        restParameterGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameterGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getParameterGroup() throws Exception {
        // Initialize the database
        parameterGroupRepository.saveAndFlush(parameterGroup);

        // Get the parameterGroup
        restParameterGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, parameterGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parameterGroup.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingParameterGroup() throws Exception {
        // Get the parameterGroup
        restParameterGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParameterGroup() throws Exception {
        // Initialize the database
        parameterGroupRepository.saveAndFlush(parameterGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parameterGroup
        ParameterGroup updatedParameterGroup = parameterGroupRepository.findById(parameterGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParameterGroup are not directly saved in db
        em.detach(updatedParameterGroup);
        updatedParameterGroup.label(UPDATED_LABEL).description(UPDATED_DESCRIPTION);

        restParameterGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParameterGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedParameterGroup))
            )
            .andExpect(status().isOk());

        // Validate the ParameterGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedParameterGroupToMatchAllProperties(updatedParameterGroup);
    }

    @Test
    @Transactional
    void putNonExistingParameterGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameterGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parameterGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(parameterGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParameterGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameterGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(parameterGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParameterGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameterGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameterGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParameterGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParameterGroupWithPatch() throws Exception {
        // Initialize the database
        parameterGroupRepository.saveAndFlush(parameterGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parameterGroup using partial update
        ParameterGroup partialUpdatedParameterGroup = new ParameterGroup();
        partialUpdatedParameterGroup.setId(parameterGroup.getId());

        partialUpdatedParameterGroup.label(UPDATED_LABEL).description(UPDATED_DESCRIPTION);

        restParameterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameterGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParameterGroup))
            )
            .andExpect(status().isOk());

        // Validate the ParameterGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParameterGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedParameterGroup, parameterGroup),
            getPersistedParameterGroup(parameterGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateParameterGroupWithPatch() throws Exception {
        // Initialize the database
        parameterGroupRepository.saveAndFlush(parameterGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parameterGroup using partial update
        ParameterGroup partialUpdatedParameterGroup = new ParameterGroup();
        partialUpdatedParameterGroup.setId(parameterGroup.getId());

        partialUpdatedParameterGroup.label(UPDATED_LABEL).description(UPDATED_DESCRIPTION);

        restParameterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameterGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParameterGroup))
            )
            .andExpect(status().isOk());

        // Validate the ParameterGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParameterGroupUpdatableFieldsEquals(partialUpdatedParameterGroup, getPersistedParameterGroup(partialUpdatedParameterGroup));
    }

    @Test
    @Transactional
    void patchNonExistingParameterGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameterGroup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parameterGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parameterGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParameterGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameterGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parameterGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParameterGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParameterGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameterGroup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(parameterGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParameterGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParameterGroup() throws Exception {
        // Initialize the database
        parameterGroupRepository.saveAndFlush(parameterGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the parameterGroup
        restParameterGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, parameterGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return parameterGroupRepository.count();
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

    protected ParameterGroup getPersistedParameterGroup(ParameterGroup parameterGroup) {
        return parameterGroupRepository.findById(parameterGroup.getId()).orElseThrow();
    }

    protected void assertPersistedParameterGroupToMatchAllProperties(ParameterGroup expectedParameterGroup) {
        assertParameterGroupAllPropertiesEquals(expectedParameterGroup, getPersistedParameterGroup(expectedParameterGroup));
    }

    protected void assertPersistedParameterGroupToMatchUpdatableProperties(ParameterGroup expectedParameterGroup) {
        assertParameterGroupAllUpdatablePropertiesEquals(expectedParameterGroup, getPersistedParameterGroup(expectedParameterGroup));
    }
}
