package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.ServiceContractAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.ServiceContract;
import io.simpleit.umbrella.repository.ServiceContractRepository;
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
 * Integration tests for the {@link ServiceContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceContractResourceIT {

    private static final String DEFAULT_SERVICE_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_LABEL = "BBBBBBBBBB";

    private static final Double DEFAULT_DAILY_RATE = 1D;
    private static final Double UPDATED_DAILY_RATE = 2D;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EXTENSION_TERMS = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION_TERMS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SIGNED_BY_SUPPLIER = false;
    private static final Boolean UPDATED_SIGNED_BY_SUPPLIER = true;

    private static final Boolean DEFAULT_SIGNED_BY_CLIENT = false;
    private static final Boolean UPDATED_SIGNED_BY_CLIENT = true;

    private static final String ENTITY_API_URL = "/api/service-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceContractRepository serviceContractRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceContractMockMvc;

    private ServiceContract serviceContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceContract createEntity(EntityManager em) {
        ServiceContract serviceContract = new ServiceContract()
            .serviceLabel(DEFAULT_SERVICE_LABEL)
            .dailyRate(DEFAULT_DAILY_RATE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .extensionTerms(DEFAULT_EXTENSION_TERMS)
            .signedBySupplier(DEFAULT_SIGNED_BY_SUPPLIER)
            .signedByClient(DEFAULT_SIGNED_BY_CLIENT);
        return serviceContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceContract createUpdatedEntity(EntityManager em) {
        ServiceContract serviceContract = new ServiceContract()
            .serviceLabel(UPDATED_SERVICE_LABEL)
            .dailyRate(UPDATED_DAILY_RATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .extensionTerms(UPDATED_EXTENSION_TERMS)
            .signedBySupplier(UPDATED_SIGNED_BY_SUPPLIER)
            .signedByClient(UPDATED_SIGNED_BY_CLIENT);
        return serviceContract;
    }

    @BeforeEach
    public void initTest() {
        serviceContract = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceContract
        var returnedServiceContract = om.readValue(
            restServiceContractMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceContract)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceContract.class
        );

        // Validate the ServiceContract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertServiceContractUpdatableFieldsEquals(returnedServiceContract, getPersistedServiceContract(returnedServiceContract));
    }

    @Test
    @Transactional
    void createServiceContractWithExistingId() throws Exception {
        // Create the ServiceContract with an existing ID
        serviceContract.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceContract)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceContract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServiceContracts() throws Exception {
        // Initialize the database
        serviceContractRepository.saveAndFlush(serviceContract);

        // Get all the serviceContractList
        restServiceContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceLabel").value(hasItem(DEFAULT_SERVICE_LABEL)))
            .andExpect(jsonPath("$.[*].dailyRate").value(hasItem(DEFAULT_DAILY_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].extensionTerms").value(hasItem(DEFAULT_EXTENSION_TERMS)))
            .andExpect(jsonPath("$.[*].signedBySupplier").value(hasItem(DEFAULT_SIGNED_BY_SUPPLIER.booleanValue())))
            .andExpect(jsonPath("$.[*].signedByClient").value(hasItem(DEFAULT_SIGNED_BY_CLIENT.booleanValue())));
    }

    @Test
    @Transactional
    void getServiceContract() throws Exception {
        // Initialize the database
        serviceContractRepository.saveAndFlush(serviceContract);

        // Get the serviceContract
        restServiceContractMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceContract.getId().intValue()))
            .andExpect(jsonPath("$.serviceLabel").value(DEFAULT_SERVICE_LABEL))
            .andExpect(jsonPath("$.dailyRate").value(DEFAULT_DAILY_RATE.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.extensionTerms").value(DEFAULT_EXTENSION_TERMS))
            .andExpect(jsonPath("$.signedBySupplier").value(DEFAULT_SIGNED_BY_SUPPLIER.booleanValue()))
            .andExpect(jsonPath("$.signedByClient").value(DEFAULT_SIGNED_BY_CLIENT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingServiceContract() throws Exception {
        // Get the serviceContract
        restServiceContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceContract() throws Exception {
        // Initialize the database
        serviceContractRepository.saveAndFlush(serviceContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceContract
        ServiceContract updatedServiceContract = serviceContractRepository.findById(serviceContract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceContract are not directly saved in db
        em.detach(updatedServiceContract);
        updatedServiceContract
            .serviceLabel(UPDATED_SERVICE_LABEL)
            .dailyRate(UPDATED_DAILY_RATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .extensionTerms(UPDATED_EXTENSION_TERMS)
            .signedBySupplier(UPDATED_SIGNED_BY_SUPPLIER)
            .signedByClient(UPDATED_SIGNED_BY_CLIENT);

        restServiceContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedServiceContract))
            )
            .andExpect(status().isOk());

        // Validate the ServiceContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceContractToMatchAllProperties(updatedServiceContract);
    }

    @Test
    @Transactional
    void putNonExistingServiceContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceContract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceContract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceContract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceContract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceContractWithPatch() throws Exception {
        // Initialize the database
        serviceContractRepository.saveAndFlush(serviceContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceContract using partial update
        ServiceContract partialUpdatedServiceContract = new ServiceContract();
        partialUpdatedServiceContract.setId(serviceContract.getId());

        partialUpdatedServiceContract
            .serviceLabel(UPDATED_SERVICE_LABEL)
            .dailyRate(UPDATED_DAILY_RATE)
            .extensionTerms(UPDATED_EXTENSION_TERMS)
            .signedByClient(UPDATED_SIGNED_BY_CLIENT);

        restServiceContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceContract))
            )
            .andExpect(status().isOk());

        // Validate the ServiceContract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceContractUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceContract, serviceContract),
            getPersistedServiceContract(serviceContract)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceContractWithPatch() throws Exception {
        // Initialize the database
        serviceContractRepository.saveAndFlush(serviceContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceContract using partial update
        ServiceContract partialUpdatedServiceContract = new ServiceContract();
        partialUpdatedServiceContract.setId(serviceContract.getId());

        partialUpdatedServiceContract
            .serviceLabel(UPDATED_SERVICE_LABEL)
            .dailyRate(UPDATED_DAILY_RATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .extensionTerms(UPDATED_EXTENSION_TERMS)
            .signedBySupplier(UPDATED_SIGNED_BY_SUPPLIER)
            .signedByClient(UPDATED_SIGNED_BY_CLIENT);

        restServiceContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceContract))
            )
            .andExpect(status().isOk());

        // Validate the ServiceContract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceContractUpdatableFieldsEquals(
            partialUpdatedServiceContract,
            getPersistedServiceContract(partialUpdatedServiceContract)
        );
    }

    @Test
    @Transactional
    void patchNonExistingServiceContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceContract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceContract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceContract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceContract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceContract() throws Exception {
        // Initialize the database
        serviceContractRepository.saveAndFlush(serviceContract);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceContract
        restServiceContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceContract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceContractRepository.count();
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

    protected ServiceContract getPersistedServiceContract(ServiceContract serviceContract) {
        return serviceContractRepository.findById(serviceContract.getId()).orElseThrow();
    }

    protected void assertPersistedServiceContractToMatchAllProperties(ServiceContract expectedServiceContract) {
        assertServiceContractAllPropertiesEquals(expectedServiceContract, getPersistedServiceContract(expectedServiceContract));
    }

    protected void assertPersistedServiceContractToMatchUpdatableProperties(ServiceContract expectedServiceContract) {
        assertServiceContractAllUpdatablePropertiesEquals(expectedServiceContract, getPersistedServiceContract(expectedServiceContract));
    }
}
