package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.ParameterAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.Parameter;
import io.simpleit.umbrella.repository.ParameterRepository;
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
 * Integration tests for the {@link ParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParameterResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParameterMockMvc;

    private Parameter parameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createEntity(EntityManager em) {
        Parameter parameter = new Parameter().label(DEFAULT_LABEL).value(DEFAULT_VALUE);
        return parameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parameter createUpdatedEntity(EntityManager em) {
        Parameter parameter = new Parameter().label(UPDATED_LABEL).value(UPDATED_VALUE);
        return parameter;
    }

    @BeforeEach
    public void initTest() {
        parameter = createEntity(em);
    }

    @Test
    @Transactional
    void createParameter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Parameter
        var returnedParameter = om.readValue(
            restParameterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameter)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Parameter.class
        );

        // Validate the Parameter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertParameterUpdatableFieldsEquals(returnedParameter, getPersistedParameter(returnedParameter));
    }

    @Test
    @Transactional
    void createParameterWithExistingId() throws Exception {
        // Create the Parameter with an existing ID
        parameter.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParameterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameter)))
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParameters() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get all the parameterList
        restParameterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        // Get the parameter
        restParameterMockMvc
            .perform(get(ENTITY_API_URL_ID, parameter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingParameter() throws Exception {
        // Get the parameter
        restParameterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parameter
        Parameter updatedParameter = parameterRepository.findById(parameter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParameter are not directly saved in db
        em.detach(updatedParameter);
        updatedParameter.label(UPDATED_LABEL).value(UPDATED_VALUE);

        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParameter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedParameterToMatchAllProperties(updatedParameter);
    }

    @Test
    @Transactional
    void putNonExistingParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parameter.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParameterWithPatch() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parameter using partial update
        Parameter partialUpdatedParameter = new Parameter();
        partialUpdatedParameter.setId(parameter.getId());

        partialUpdatedParameter.label(UPDATED_LABEL);

        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParameterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedParameter, parameter),
            getPersistedParameter(parameter)
        );
    }

    @Test
    @Transactional
    void fullUpdateParameterWithPatch() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parameter using partial update
        Parameter partialUpdatedParameter = new Parameter();
        partialUpdatedParameter.setId(parameter.getId());

        partialUpdatedParameter.label(UPDATED_LABEL).value(UPDATED_VALUE);

        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParameter))
            )
            .andExpect(status().isOk());

        // Validate the Parameter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParameterUpdatableFieldsEquals(partialUpdatedParameter, getPersistedParameter(partialUpdatedParameter));
    }

    @Test
    @Transactional
    void patchNonExistingParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parameter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parameter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParameter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parameter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParameterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(parameter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parameter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParameter() throws Exception {
        // Initialize the database
        parameterRepository.saveAndFlush(parameter);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the parameter
        restParameterMockMvc
            .perform(delete(ENTITY_API_URL_ID, parameter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return parameterRepository.count();
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

    protected Parameter getPersistedParameter(Parameter parameter) {
        return parameterRepository.findById(parameter.getId()).orElseThrow();
    }

    protected void assertPersistedParameterToMatchAllProperties(Parameter expectedParameter) {
        assertParameterAllPropertiesEquals(expectedParameter, getPersistedParameter(expectedParameter));
    }

    protected void assertPersistedParameterToMatchUpdatableProperties(Parameter expectedParameter) {
        assertParameterAllUpdatablePropertiesEquals(expectedParameter, getPersistedParameter(expectedParameter));
    }
}
