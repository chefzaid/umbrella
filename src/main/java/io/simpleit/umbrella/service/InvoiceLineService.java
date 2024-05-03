package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.InvoiceLine;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.InvoiceLine}.
 */
public interface InvoiceLineService {
    /**
     * Save a invoiceLine.
     *
     * @param invoiceLine the entity to save.
     * @return the persisted entity.
     */
    InvoiceLine save(InvoiceLine invoiceLine);

    /**
     * Updates a invoiceLine.
     *
     * @param invoiceLine the entity to update.
     * @return the persisted entity.
     */
    InvoiceLine update(InvoiceLine invoiceLine);

    /**
     * Partially updates a invoiceLine.
     *
     * @param invoiceLine the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceLine> partialUpdate(InvoiceLine invoiceLine);

    /**
     * Get all the invoiceLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceLine> findAll(Pageable pageable);

    /**
     * Get the "id" invoiceLine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceLine> findOne(Long id);

    /**
     * Delete the "id" invoiceLine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
