package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.IdDocument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IdDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdDocumentRepository extends JpaRepository<IdDocument, Long> {}
