package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.TimeSheetLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimeSheetLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeSheetLineRepository extends JpaRepository<TimeSheetLine, Long> {}
