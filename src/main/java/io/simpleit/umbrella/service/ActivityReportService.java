package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.ActivityReport;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.ActivityReport}.
 */
public interface ActivityReportService {
    /**
     * Save a activityReport.
     *
     * @param activityReport the entity to save.
     * @return the persisted entity.
     */
    ActivityReport save(ActivityReport activityReport);

    /**
     * Updates a activityReport.
     *
     * @param activityReport the entity to update.
     * @return the persisted entity.
     */
    ActivityReport update(ActivityReport activityReport);

    /**
     * Partially updates a activityReport.
     *
     * @param activityReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ActivityReport> partialUpdate(ActivityReport activityReport);

    /**
     * Get all the activityReports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActivityReport> findAll(Pageable pageable);

    /**
     * Get the "id" activityReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActivityReport> findOne(Long id);

    /**
     * Delete the "id" activityReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
