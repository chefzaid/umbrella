package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.Formula;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.Formula}.
 */
public interface FormulaService {
    /**
     * Save a formula.
     *
     * @param formula the entity to save.
     * @return the persisted entity.
     */
    Formula save(Formula formula);

    /**
     * Updates a formula.
     *
     * @param formula the entity to update.
     * @return the persisted entity.
     */
    Formula update(Formula formula);

    /**
     * Partially updates a formula.
     *
     * @param formula the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Formula> partialUpdate(Formula formula);

    /**
     * Get all the formulas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Formula> findAll(Pageable pageable);

    /**
     * Get all the Formula where Prospect is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Formula> findAllWhereProspectIsNull();
    /**
     * Get all the Formula where EmploymentContract is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Formula> findAllWhereEmploymentContractIsNull();

    /**
     * Get the "id" formula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Formula> findOne(Long id);

    /**
     * Delete the "id" formula.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
