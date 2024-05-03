package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.ExpenseNote;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.ExpenseNote}.
 */
public interface ExpenseNoteService {
    /**
     * Save a expenseNote.
     *
     * @param expenseNote the entity to save.
     * @return the persisted entity.
     */
    ExpenseNote save(ExpenseNote expenseNote);

    /**
     * Updates a expenseNote.
     *
     * @param expenseNote the entity to update.
     * @return the persisted entity.
     */
    ExpenseNote update(ExpenseNote expenseNote);

    /**
     * Partially updates a expenseNote.
     *
     * @param expenseNote the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExpenseNote> partialUpdate(ExpenseNote expenseNote);

    /**
     * Get all the expenseNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExpenseNote> findAll(Pageable pageable);

    /**
     * Get the "id" expenseNote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExpenseNote> findOne(Long id);

    /**
     * Delete the "id" expenseNote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
