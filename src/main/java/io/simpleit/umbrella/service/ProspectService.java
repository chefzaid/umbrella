package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.Prospect;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.Prospect}.
 */
public interface ProspectService {
    /**
     * Save a prospect.
     *
     * @param prospect the entity to save.
     * @return the persisted entity.
     */
    Prospect save(Prospect prospect);

    /**
     * Updates a prospect.
     *
     * @param prospect the entity to update.
     * @return the persisted entity.
     */
    Prospect update(Prospect prospect);

    /**
     * Partially updates a prospect.
     *
     * @param prospect the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Prospect> partialUpdate(Prospect prospect);

    /**
     * Get all the prospects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Prospect> findAll(Pageable pageable);

    /**
     * Get the "id" prospect.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Prospect> findOne(Long id);

    /**
     * Delete the "id" prospect.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
