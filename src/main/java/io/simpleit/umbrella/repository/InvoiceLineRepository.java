package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.InvoiceLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {}
