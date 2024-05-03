package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.Project}.
 */
public interface ProjectService {
    /**
     * Save a project.
     *
     * @param project the entity to save.
     * @return the persisted entity.
     */
    Project save(Project project);

    /**
     * Updates a project.
     *
     * @param project the entity to update.
     * @return the persisted entity.
     */
    Project update(Project project);

    /**
     * Partially updates a project.
     *
     * @param project the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Project> partialUpdate(Project project);

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Project> findAll(Pageable pageable);

    /**
     * Get all the Project where TimeSheet is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Project> findAllWhereTimeSheetIsNull();
    /**
     * Get all the Project where Expense is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Project> findAllWhereExpenseIsNull();
    /**
     * Get all the Project where Invoice is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Project> findAllWhereInvoiceIsNull();

    /**
     * Get the "id" project.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Project> findOne(Long id);

    /**
     * Delete the "id" project.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
