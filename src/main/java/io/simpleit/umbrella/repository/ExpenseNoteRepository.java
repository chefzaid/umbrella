package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.ExpenseNote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExpenseNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpenseNoteRepository extends JpaRepository<ExpenseNote, Long> {}
