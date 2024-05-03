package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.Enterprise;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.Enterprise}.
 */
public interface EnterpriseService {
    /**
     * Save a enterprise.
     *
     * @param enterprise the entity to save.
     * @return the persisted entity.
     */
    Enterprise save(Enterprise enterprise);

    /**
     * Updates a enterprise.
     *
     * @param enterprise the entity to update.
     * @return the persisted entity.
     */
    Enterprise update(Enterprise enterprise);

    /**
     * Partially updates a enterprise.
     *
     * @param enterprise the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Enterprise> partialUpdate(Enterprise enterprise);

    /**
     * Get all the enterprises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Enterprise> findAll(Pageable pageable);

    /**
     * Get the "id" enterprise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Enterprise> findOne(Long id);

    /**
     * Delete the "id" enterprise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
