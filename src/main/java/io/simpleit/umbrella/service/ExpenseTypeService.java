package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.ExpenseType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.ExpenseType}.
 */
public interface ExpenseTypeService {
    /**
     * Save a expenseType.
     *
     * @param expenseType the entity to save.
     * @return the persisted entity.
     */
    ExpenseType save(ExpenseType expenseType);

    /**
     * Updates a expenseType.
     *
     * @param expenseType the entity to update.
     * @return the persisted entity.
     */
    ExpenseType update(ExpenseType expenseType);

    /**
     * Partially updates a expenseType.
     *
     * @param expenseType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExpenseType> partialUpdate(ExpenseType expenseType);

    /**
     * Get all the expenseTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExpenseType> findAll(Pageable pageable);

    /**
     * Get the "id" expenseType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExpenseType> findOne(Long id);

    /**
     * Delete the "id" expenseType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
