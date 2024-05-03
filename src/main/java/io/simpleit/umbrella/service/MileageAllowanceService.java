package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.MileageAllowance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.MileageAllowance}.
 */
public interface MileageAllowanceService {
    /**
     * Save a mileageAllowance.
     *
     * @param mileageAllowance the entity to save.
     * @return the persisted entity.
     */
    MileageAllowance save(MileageAllowance mileageAllowance);

    /**
     * Updates a mileageAllowance.
     *
     * @param mileageAllowance the entity to update.
     * @return the persisted entity.
     */
    MileageAllowance update(MileageAllowance mileageAllowance);

    /**
     * Partially updates a mileageAllowance.
     *
     * @param mileageAllowance the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MileageAllowance> partialUpdate(MileageAllowance mileageAllowance);

    /**
     * Get all the mileageAllowances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MileageAllowance> findAll(Pageable pageable);

    /**
     * Get all the MileageAllowance where ExpenseNote is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<MileageAllowance> findAllWhereExpenseNoteIsNull();

    /**
     * Get the "id" mileageAllowance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MileageAllowance> findOne(Long id);

    /**
     * Delete the "id" mileageAllowance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
