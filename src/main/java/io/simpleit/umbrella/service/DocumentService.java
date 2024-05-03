package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.Document;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.Document}.
 */
public interface DocumentService {
    /**
     * Save a document.
     *
     * @param document the entity to save.
     * @return the persisted entity.
     */
    Document save(Document document);

    /**
     * Updates a document.
     *
     * @param document the entity to update.
     * @return the persisted entity.
     */
    Document update(Document document);

    /**
     * Partially updates a document.
     *
     * @param document the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Document> partialUpdate(Document document);

    /**
     * Get all the documents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Document> findAll(Pageable pageable);

    /**
     * Get all the Document where IdDocument is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Document> findAllWhereIdDocumentIsNull();
    /**
     * Get all the Document where TimeSheet is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Document> findAllWhereTimeSheetIsNull();
    /**
     * Get all the Document where ExpenseNote is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Document> findAllWhereExpenseNoteIsNull();
    /**
     * Get all the Document where Invoice is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Document> findAllWhereInvoiceIsNull();
    /**
     * Get all the Document where PaySlip is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Document> findAllWherePaySlipIsNull();

    /**
     * Get the "id" document.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Document> findOne(Long id);

    /**
     * Delete the "id" document.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
