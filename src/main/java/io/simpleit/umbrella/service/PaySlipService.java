package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.PaySlip;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.PaySlip}.
 */
public interface PaySlipService {
    /**
     * Save a paySlip.
     *
     * @param paySlip the entity to save.
     * @return the persisted entity.
     */
    PaySlip save(PaySlip paySlip);

    /**
     * Updates a paySlip.
     *
     * @param paySlip the entity to update.
     * @return the persisted entity.
     */
    PaySlip update(PaySlip paySlip);

    /**
     * Partially updates a paySlip.
     *
     * @param paySlip the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaySlip> partialUpdate(PaySlip paySlip);

    /**
     * Get all the paySlips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaySlip> findAll(Pageable pageable);

    /**
     * Get the "id" paySlip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaySlip> findOne(Long id);

    /**
     * Delete the "id" paySlip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
