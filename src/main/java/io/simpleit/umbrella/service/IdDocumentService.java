package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.IdDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.IdDocument}.
 */
public interface IdDocumentService {
    /**
     * Save a idDocument.
     *
     * @param idDocument the entity to save.
     * @return the persisted entity.
     */
    IdDocument save(IdDocument idDocument);

    /**
     * Updates a idDocument.
     *
     * @param idDocument the entity to update.
     * @return the persisted entity.
     */
    IdDocument update(IdDocument idDocument);

    /**
     * Partially updates a idDocument.
     *
     * @param idDocument the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IdDocument> partialUpdate(IdDocument idDocument);

    /**
     * Get all the idDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IdDocument> findAll(Pageable pageable);

    /**
     * Get all the IdDocument where Employee is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<IdDocument> findAllWhereEmployeeIsNull();

    /**
     * Get the "id" idDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IdDocument> findOne(Long id);

    /**
     * Delete the "id" idDocument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
