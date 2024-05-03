package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.ActivityReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ActivityReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityReportRepository extends JpaRepository<ActivityReport, Long> {}
