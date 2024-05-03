package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.EmploymentContractAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.EmploymentContract;
import io.simpleit.umbrella.domain.enumeration.EmploymentContractType;
import io.simpleit.umbrella.repository.EmploymentContractRepository;
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
 * Integration tests for the {@link EmploymentContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmploymentContractResourceIT {

    private static final EmploymentContractType DEFAULT_TYPE = EmploymentContractType.PERMNANENT;
    private static final EmploymentContractType UPDATED_TYPE = EmploymentContractType.TEMPORARY;

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_HIRE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HIRE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_SALARY = 1D;
    private static final Double UPDATED_SALARY = 2D;

    private static final Long DEFAULT_YEARLY_WORK_DAYS = 1L;
    private static final Long UPDATED_YEARLY_WORK_DAYS = 2L;

    private static final Boolean DEFAULT_SIGNED_BY_EMPLOYER = false;
    private static final Boolean UPDATED_SIGNED_BY_EMPLOYER = true;

    private static final Boolean DEFAULT_SIGNED_BY_EMPLOYEE = false;
    private static final Boolean UPDATED_SIGNED_BY_EMPLOYEE = true;

    private static final String ENTITY_API_URL = "/api/employment-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmploymentContractRepository employmentContractRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentContractMockMvc;

    private EmploymentContract employmentContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentContract createEntity(EntityManager em) {
        EmploymentContract employmentContract = new EmploymentContract()
            .type(DEFAULT_TYPE)
            .jobTitle(DEFAULT_JOB_TITLE)
            .hireDate(DEFAULT_HIRE_DATE)
            .salary(DEFAULT_SALARY)
            .yearlyWorkDays(DEFAULT_YEARLY_WORK_DAYS)
            .signedByEmployer(DEFAULT_SIGNED_BY_EMPLOYER)
            .signedByEmployee(DEFAULT_SIGNED_BY_EMPLOYEE);
        return employmentContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmploymentContract createUpdatedEntity(EntityManager em) {
        EmploymentContract employmentContract = new EmploymentContract()
            .type(UPDATED_TYPE)
            .jobTitle(UPDATED_JOB_TITLE)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .yearlyWorkDays(UPDATED_YEARLY_WORK_DAYS)
            .signedByEmployer(UPDATED_SIGNED_BY_EMPLOYER)
            .signedByEmployee(UPDATED_SIGNED_BY_EMPLOYEE);
        return employmentContract;
    }

    @BeforeEach
    public void initTest() {
        employmentContract = createEntity(em);
    }

    @Test
    @Transactional
    void createEmploymentContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmploymentContract
        var returnedEmploymentContract = om.readValue(
            restEmploymentContractMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employmentContract)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmploymentContract.class
        );

        // Validate the EmploymentContract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmploymentContractUpdatableFieldsEquals(
            returnedEmploymentContract,
            getPersistedEmploymentContract(returnedEmploymentContract)
        );
    }

    @Test
    @Transactional
    void createEmploymentContractWithExistingId() throws Exception {
        // Create the EmploymentContract with an existing ID
        employmentContract.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employmentContract)))
            .andExpect(status().isBadRequest());

        // Validate the EmploymentContract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmploymentContracts() throws Exception {
        // Initialize the database
        employmentContractRepository.saveAndFlush(employmentContract);

        // Get all the employmentContractList
        restEmploymentContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employmentContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].yearlyWorkDays").value(hasItem(DEFAULT_YEARLY_WORK_DAYS.intValue())))
            .andExpect(jsonPath("$.[*].signedByEmployer").value(hasItem(DEFAULT_SIGNED_BY_EMPLOYER.booleanValue())))
            .andExpect(jsonPath("$.[*].signedByEmployee").value(hasItem(DEFAULT_SIGNED_BY_EMPLOYEE.booleanValue())));
    }

    @Test
    @Transactional
    void getEmploymentContract() throws Exception {
        // Initialize the database
        employmentContractRepository.saveAndFlush(employmentContract);

        // Get the employmentContract
        restEmploymentContractMockMvc
            .perform(get(ENTITY_API_URL_ID, employmentContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employmentContract.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.doubleValue()))
            .andExpect(jsonPath("$.yearlyWorkDays").value(DEFAULT_YEARLY_WORK_DAYS.intValue()))
            .andExpect(jsonPath("$.signedByEmployer").value(DEFAULT_SIGNED_BY_EMPLOYER.booleanValue()))
            .andExpect(jsonPath("$.signedByEmployee").value(DEFAULT_SIGNED_BY_EMPLOYEE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEmploymentContract() throws Exception {
        // Get the employmentContract
        restEmploymentContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmploymentContract() throws Exception {
        // Initialize the database
        employmentContractRepository.saveAndFlush(employmentContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employmentContract
        EmploymentContract updatedEmploymentContract = employmentContractRepository.findById(employmentContract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmploymentContract are not directly saved in db
        em.detach(updatedEmploymentContract);
        updatedEmploymentContract
            .type(UPDATED_TYPE)
            .jobTitle(UPDATED_JOB_TITLE)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .yearlyWorkDays(UPDATED_YEARLY_WORK_DAYS)
            .signedByEmployer(UPDATED_SIGNED_BY_EMPLOYER)
            .signedByEmployee(UPDATED_SIGNED_BY_EMPLOYEE);

        restEmploymentContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmploymentContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmploymentContract))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmploymentContractToMatchAllProperties(updatedEmploymentContract);
    }

    @Test
    @Transactional
    void putNonExistingEmploymentContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employmentContract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employmentContract.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employmentContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploymentContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employmentContract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employmentContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploymentContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employmentContract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employmentContract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentContractWithPatch() throws Exception {
        // Initialize the database
        employmentContractRepository.saveAndFlush(employmentContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employmentContract using partial update
        EmploymentContract partialUpdatedEmploymentContract = new EmploymentContract();
        partialUpdatedEmploymentContract.setId(employmentContract.getId());

        partialUpdatedEmploymentContract
            .jobTitle(UPDATED_JOB_TITLE)
            .hireDate(UPDATED_HIRE_DATE)
            .yearlyWorkDays(UPDATED_YEARLY_WORK_DAYS)
            .signedByEmployee(UPDATED_SIGNED_BY_EMPLOYEE);

        restEmploymentContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmploymentContract))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentContract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmploymentContractUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmploymentContract, employmentContract),
            getPersistedEmploymentContract(employmentContract)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmploymentContractWithPatch() throws Exception {
        // Initialize the database
        employmentContractRepository.saveAndFlush(employmentContract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employmentContract using partial update
        EmploymentContract partialUpdatedEmploymentContract = new EmploymentContract();
        partialUpdatedEmploymentContract.setId(employmentContract.getId());

        partialUpdatedEmploymentContract
            .type(UPDATED_TYPE)
            .jobTitle(UPDATED_JOB_TITLE)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .yearlyWorkDays(UPDATED_YEARLY_WORK_DAYS)
            .signedByEmployer(UPDATED_SIGNED_BY_EMPLOYER)
            .signedByEmployee(UPDATED_SIGNED_BY_EMPLOYEE);

        restEmploymentContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploymentContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmploymentContract))
            )
            .andExpect(status().isOk());

        // Validate the EmploymentContract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmploymentContractUpdatableFieldsEquals(
            partialUpdatedEmploymentContract,
            getPersistedEmploymentContract(partialUpdatedEmploymentContract)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmploymentContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employmentContract.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employmentContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employmentContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploymentContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employmentContract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employmentContract))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmploymentContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploymentContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employmentContract.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employmentContract)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmploymentContract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmploymentContract() throws Exception {
        // Initialize the database
        employmentContractRepository.saveAndFlush(employmentContract);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employmentContract
        restEmploymentContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, employmentContract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employmentContractRepository.count();
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

    protected EmploymentContract getPersistedEmploymentContract(EmploymentContract employmentContract) {
        return employmentContractRepository.findById(employmentContract.getId()).orElseThrow();
    }

    protected void assertPersistedEmploymentContractToMatchAllProperties(EmploymentContract expectedEmploymentContract) {
        assertEmploymentContractAllPropertiesEquals(expectedEmploymentContract, getPersistedEmploymentContract(expectedEmploymentContract));
    }

    protected void assertPersistedEmploymentContractToMatchUpdatableProperties(EmploymentContract expectedEmploymentContract) {
        assertEmploymentContractAllUpdatablePropertiesEquals(
            expectedEmploymentContract,
            getPersistedEmploymentContract(expectedEmploymentContract)
        );
    }
}
