package io.simpleit.umbrella.web.rest;

import static io.simpleit.umbrella.domain.PaySlipAsserts.*;
import static io.simpleit.umbrella.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpleit.umbrella.IntegrationTest;
import io.simpleit.umbrella.domain.PaySlip;
import io.simpleit.umbrella.repository.PaySlipRepository;
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
 * Integration tests for the {@link PaySlipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaySlipResourceIT {

    private static final Double DEFAULT_SUPER_GROSS_SALARY = 1D;
    private static final Double UPDATED_SUPER_GROSS_SALARY = 2D;

    private static final Double DEFAULT_GROSS_SALARY = 1D;
    private static final Double UPDATED_GROSS_SALARY = 2D;

    private static final Double DEFAULT_NET_SALARY = 1D;
    private static final Double UPDATED_NET_SALARY = 2D;

    private static final Double DEFAULT_TAX_RATE = 1D;
    private static final Double UPDATED_TAX_RATE = 2D;

    private static final Double DEFAULT_AMOUNT_PAID = 1D;
    private static final Double UPDATED_AMOUNT_PAID = 2D;

    private static final Double DEFAULT_TOTAL_EXPENSES = 1D;
    private static final Double UPDATED_TOTAL_EXPENSES = 2D;

    private static final String ENTITY_API_URL = "/api/pay-slips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaySlipRepository paySlipRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaySlipMockMvc;

    private PaySlip paySlip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaySlip createEntity(EntityManager em) {
        PaySlip paySlip = new PaySlip()
            .superGrossSalary(DEFAULT_SUPER_GROSS_SALARY)
            .grossSalary(DEFAULT_GROSS_SALARY)
            .netSalary(DEFAULT_NET_SALARY)
            .taxRate(DEFAULT_TAX_RATE)
            .amountPaid(DEFAULT_AMOUNT_PAID)
            .totalExpenses(DEFAULT_TOTAL_EXPENSES);
        return paySlip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaySlip createUpdatedEntity(EntityManager em) {
        PaySlip paySlip = new PaySlip()
            .superGrossSalary(UPDATED_SUPER_GROSS_SALARY)
            .grossSalary(UPDATED_GROSS_SALARY)
            .netSalary(UPDATED_NET_SALARY)
            .taxRate(UPDATED_TAX_RATE)
            .amountPaid(UPDATED_AMOUNT_PAID)
            .totalExpenses(UPDATED_TOTAL_EXPENSES);
        return paySlip;
    }

    @BeforeEach
    public void initTest() {
        paySlip = createEntity(em);
    }

    @Test
    @Transactional
    void createPaySlip() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaySlip
        var returnedPaySlip = om.readValue(
            restPaySlipMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlip)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaySlip.class
        );

        // Validate the PaySlip in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaySlipUpdatableFieldsEquals(returnedPaySlip, getPersistedPaySlip(returnedPaySlip));
    }

    @Test
    @Transactional
    void createPaySlipWithExistingId() throws Exception {
        // Create the PaySlip with an existing ID
        paySlip.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaySlipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlip)))
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaySlips() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        // Get all the paySlipList
        restPaySlipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paySlip.getId().intValue())))
            .andExpect(jsonPath("$.[*].superGrossSalary").value(hasItem(DEFAULT_SUPER_GROSS_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].grossSalary").value(hasItem(DEFAULT_GROSS_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].netSalary").value(hasItem(DEFAULT_NET_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].taxRate").value(hasItem(DEFAULT_TAX_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].amountPaid").value(hasItem(DEFAULT_AMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].totalExpenses").value(hasItem(DEFAULT_TOTAL_EXPENSES.doubleValue())));
    }

    @Test
    @Transactional
    void getPaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        // Get the paySlip
        restPaySlipMockMvc
            .perform(get(ENTITY_API_URL_ID, paySlip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paySlip.getId().intValue()))
            .andExpect(jsonPath("$.superGrossSalary").value(DEFAULT_SUPER_GROSS_SALARY.doubleValue()))
            .andExpect(jsonPath("$.grossSalary").value(DEFAULT_GROSS_SALARY.doubleValue()))
            .andExpect(jsonPath("$.netSalary").value(DEFAULT_NET_SALARY.doubleValue()))
            .andExpect(jsonPath("$.taxRate").value(DEFAULT_TAX_RATE.doubleValue()))
            .andExpect(jsonPath("$.amountPaid").value(DEFAULT_AMOUNT_PAID.doubleValue()))
            .andExpect(jsonPath("$.totalExpenses").value(DEFAULT_TOTAL_EXPENSES.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPaySlip() throws Exception {
        // Get the paySlip
        restPaySlipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paySlip
        PaySlip updatedPaySlip = paySlipRepository.findById(paySlip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaySlip are not directly saved in db
        em.detach(updatedPaySlip);
        updatedPaySlip
            .superGrossSalary(UPDATED_SUPER_GROSS_SALARY)
            .grossSalary(UPDATED_GROSS_SALARY)
            .netSalary(UPDATED_NET_SALARY)
            .taxRate(UPDATED_TAX_RATE)
            .amountPaid(UPDATED_AMOUNT_PAID)
            .totalExpenses(UPDATED_TOTAL_EXPENSES);

        restPaySlipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaySlip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaySlip))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaySlipToMatchAllProperties(updatedPaySlip);
    }

    @Test
    @Transactional
    void putNonExistingPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(put(ENTITY_API_URL_ID, paySlip.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlip)))
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paySlip))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaySlipWithPatch() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paySlip using partial update
        PaySlip partialUpdatedPaySlip = new PaySlip();
        partialUpdatedPaySlip.setId(paySlip.getId());

        partialUpdatedPaySlip
            .superGrossSalary(UPDATED_SUPER_GROSS_SALARY)
            .amountPaid(UPDATED_AMOUNT_PAID)
            .totalExpenses(UPDATED_TOTAL_EXPENSES);

        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaySlip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaySlip))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaySlipUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaySlip, paySlip), getPersistedPaySlip(paySlip));
    }

    @Test
    @Transactional
    void fullUpdatePaySlipWithPatch() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paySlip using partial update
        PaySlip partialUpdatedPaySlip = new PaySlip();
        partialUpdatedPaySlip.setId(paySlip.getId());

        partialUpdatedPaySlip
            .superGrossSalary(UPDATED_SUPER_GROSS_SALARY)
            .grossSalary(UPDATED_GROSS_SALARY)
            .netSalary(UPDATED_NET_SALARY)
            .taxRate(UPDATED_TAX_RATE)
            .amountPaid(UPDATED_AMOUNT_PAID)
            .totalExpenses(UPDATED_TOTAL_EXPENSES);

        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaySlip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaySlip))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaySlipUpdatableFieldsEquals(partialUpdatedPaySlip, getPersistedPaySlip(partialUpdatedPaySlip));
    }

    @Test
    @Transactional
    void patchNonExistingPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paySlip.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paySlip))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paySlip))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paySlip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.saveAndFlush(paySlip);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paySlip
        restPaySlipMockMvc
            .perform(delete(ENTITY_API_URL_ID, paySlip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paySlipRepository.count();
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

    protected PaySlip getPersistedPaySlip(PaySlip paySlip) {
        return paySlipRepository.findById(paySlip.getId()).orElseThrow();
    }

    protected void assertPersistedPaySlipToMatchAllProperties(PaySlip expectedPaySlip) {
        assertPaySlipAllPropertiesEquals(expectedPaySlip, getPersistedPaySlip(expectedPaySlip));
    }

    protected void assertPersistedPaySlipToMatchUpdatableProperties(PaySlip expectedPaySlip) {
        assertPaySlipAllUpdatablePropertiesEquals(expectedPaySlip, getPersistedPaySlip(expectedPaySlip));
    }
}
