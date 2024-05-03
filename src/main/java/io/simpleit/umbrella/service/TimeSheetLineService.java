package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.TimeSheetLine;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.TimeSheetLine}.
 */
public interface TimeSheetLineService {
    /**
     * Save a timeSheetLine.
     *
     * @param timeSheetLine the entity to save.
     * @return the persisted entity.
     */
    TimeSheetLine save(TimeSheetLine timeSheetLine);

    /**
     * Updates a timeSheetLine.
     *
     * @param timeSheetLine the entity to update.
     * @return the persisted entity.
     */
    TimeSheetLine update(TimeSheetLine timeSheetLine);

    /**
     * Partially updates a timeSheetLine.
     *
     * @param timeSheetLine the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TimeSheetLine> partialUpdate(TimeSheetLine timeSheetLine);

    /**
     * Get all the timeSheetLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TimeSheetLine> findAll(Pageable pageable);

    /**
     * Get the "id" timeSheetLine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimeSheetLine> findOne(Long id);

    /**
     * Delete the "id" timeSheetLine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
