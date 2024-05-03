package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.Formula;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Formula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormulaRepository extends JpaRepository<Formula, Long> {}
