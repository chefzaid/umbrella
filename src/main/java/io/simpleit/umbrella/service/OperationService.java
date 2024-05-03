package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.Operation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.Operation}.
 */
public interface OperationService {
    /**
     * Save a operation.
     *
     * @param operation the entity to save.
     * @return the persisted entity.
     */
    Operation save(Operation operation);

    /**
     * Updates a operation.
     *
     * @param operation the entity to update.
     * @return the persisted entity.
     */
    Operation update(Operation operation);

    /**
     * Partially updates a operation.
     *
     * @param operation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Operation> partialUpdate(Operation operation);

    /**
     * Get all the operations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Operation> findAll(Pageable pageable);

    /**
     * Get the "id" operation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Operation> findOne(Long id);

    /**
     * Delete the "id" operation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
