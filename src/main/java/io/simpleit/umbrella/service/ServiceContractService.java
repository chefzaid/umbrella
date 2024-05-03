package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.ServiceContract;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.ServiceContract}.
 */
public interface ServiceContractService {
    /**
     * Save a serviceContract.
     *
     * @param serviceContract the entity to save.
     * @return the persisted entity.
     */
    ServiceContract save(ServiceContract serviceContract);

    /**
     * Updates a serviceContract.
     *
     * @param serviceContract the entity to update.
     * @return the persisted entity.
     */
    ServiceContract update(ServiceContract serviceContract);

    /**
     * Partially updates a serviceContract.
     *
     * @param serviceContract the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceContract> partialUpdate(ServiceContract serviceContract);

    /**
     * Get all the serviceContracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServiceContract> findAll(Pageable pageable);

    /**
     * Get the "id" serviceContract.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceContract> findOne(Long id);

    /**
     * Delete the "id" serviceContract.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
