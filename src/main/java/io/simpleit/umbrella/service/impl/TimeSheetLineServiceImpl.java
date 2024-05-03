package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.TimeSheetLine;
import io.simpleit.umbrella.repository.TimeSheetLineRepository;
import io.simpleit.umbrella.service.TimeSheetLineService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.TimeSheetLine}.
 */
@Service
@Transactional
public class TimeSheetLineServiceImpl implements TimeSheetLineService {

    private final Logger log = LoggerFactory.getLogger(TimeSheetLineServiceImpl.class);

    private final TimeSheetLineRepository timeSheetLineRepository;

    public TimeSheetLineServiceImpl(TimeSheetLineRepository timeSheetLineRepository) {
        this.timeSheetLineRepository = timeSheetLineRepository;
    }

    @Override
    public TimeSheetLine save(TimeSheetLine timeSheetLine) {
        log.debug("Request to save TimeSheetLine : {}", timeSheetLine);
        return timeSheetLineRepository.save(timeSheetLine);
    }

    @Override
    public TimeSheetLine update(TimeSheetLine timeSheetLine) {
        log.debug("Request to update TimeSheetLine : {}", timeSheetLine);
        return timeSheetLineRepository.save(timeSheetLine);
    }

    @Override
    public Optional<TimeSheetLine> partialUpdate(TimeSheetLine timeSheetLine) {
        log.debug("Request to partially update TimeSheetLine : {}", timeSheetLine);

        return timeSheetLineRepository
            .findById(timeSheetLine.getId())
            .map(existingTimeSheetLine -> {
                if (timeSheetLine.getMonthlyDays() != null) {
                    existingTimeSheetLine.setMonthlyDays(timeSheetLine.getMonthlyDays());
                }
                if (timeSheetLine.getExtraHours() != null) {
                    existingTimeSheetLine.setExtraHours(timeSheetLine.getExtraHours());
                }
                if (timeSheetLine.getComments() != null) {
                    existingTimeSheetLine.setComments(timeSheetLine.getComments());
                }

                return existingTimeSheetLine;
            })
            .map(timeSheetLineRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TimeSheetLine> findAll(Pageable pageable) {
        log.debug("Request to get all TimeSheetLines");
        return timeSheetLineRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeSheetLine> findOne(Long id) {
        log.debug("Request to get TimeSheetLine : {}", id);
        return timeSheetLineRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimeSheetLine : {}", id);
        timeSheetLineRepository.deleteById(id);
    }
}
