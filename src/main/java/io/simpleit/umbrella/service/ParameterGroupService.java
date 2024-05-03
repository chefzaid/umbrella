package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.ParameterGroup;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.ParameterGroup}.
 */
public interface ParameterGroupService {
    /**
     * Save a parameterGroup.
     *
     * @param parameterGroup the entity to save.
     * @return the persisted entity.
     */
    ParameterGroup save(ParameterGroup parameterGroup);

    /**
     * Updates a parameterGroup.
     *
     * @param parameterGroup the entity to update.
     * @return the persisted entity.
     */
    ParameterGroup update(ParameterGroup parameterGroup);

    /**
     * Partially updates a parameterGroup.
     *
     * @param parameterGroup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ParameterGroup> partialUpdate(ParameterGroup parameterGroup);

    /**
     * Get all the parameterGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParameterGroup> findAll(Pageable pageable);

    /**
     * Get the "id" parameterGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParameterGroup> findOne(Long id);

    /**
     * Delete the "id" parameterGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
