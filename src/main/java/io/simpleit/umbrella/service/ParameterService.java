package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.Parameter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.Parameter}.
 */
public interface ParameterService {
    /**
     * Save a parameter.
     *
     * @param parameter the entity to save.
     * @return the persisted entity.
     */
    Parameter save(Parameter parameter);

    /**
     * Updates a parameter.
     *
     * @param parameter the entity to update.
     * @return the persisted entity.
     */
    Parameter update(Parameter parameter);

    /**
     * Partially updates a parameter.
     *
     * @param parameter the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Parameter> partialUpdate(Parameter parameter);

    /**
     * Get all the parameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Parameter> findAll(Pageable pageable);

    /**
     * Get all the Parameter where Document is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Parameter> findAllWhereDocumentIsNull();
    /**
     * Get all the Parameter where Expense is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Parameter> findAllWhereExpenseIsNull();

    /**
     * Get the "id" parameter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Parameter> findOne(Long id);

    /**
     * Delete the "id" parameter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
