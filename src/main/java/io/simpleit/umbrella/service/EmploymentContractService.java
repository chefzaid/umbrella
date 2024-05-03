package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.EmploymentContract;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.EmploymentContract}.
 */
public interface EmploymentContractService {
    /**
     * Save a employmentContract.
     *
     * @param employmentContract the entity to save.
     * @return the persisted entity.
     */
    EmploymentContract save(EmploymentContract employmentContract);

    /**
     * Updates a employmentContract.
     *
     * @param employmentContract the entity to update.
     * @return the persisted entity.
     */
    EmploymentContract update(EmploymentContract employmentContract);

    /**
     * Partially updates a employmentContract.
     *
     * @param employmentContract the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmploymentContract> partialUpdate(EmploymentContract employmentContract);

    /**
     * Get all the employmentContracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmploymentContract> findAll(Pageable pageable);

    /**
     * Get all the EmploymentContract where Employee is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<EmploymentContract> findAllWhereEmployeeIsNull();

    /**
     * Get the "id" employmentContract.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmploymentContract> findOne(Long id);

    /**
     * Delete the "id" employmentContract.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
