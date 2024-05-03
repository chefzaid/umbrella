package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.ActivityReport;
import io.simpleit.umbrella.repository.ActivityReportRepository;
import io.simpleit.umbrella.service.ActivityReportService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.ActivityReport}.
 */
@Service
@Transactional
public class ActivityReportServiceImpl implements ActivityReportService {

    private final Logger log = LoggerFactory.getLogger(ActivityReportServiceImpl.class);

    private final ActivityReportRepository activityReportRepository;

    public ActivityReportServiceImpl(ActivityReportRepository activityReportRepository) {
        this.activityReportRepository = activityReportRepository;
    }

    @Override
    public ActivityReport save(ActivityReport activityReport) {
        log.debug("Request to save ActivityReport : {}", activityReport);
        return activityReportRepository.save(activityReport);
    }

    @Override
    public ActivityReport update(ActivityReport activityReport) {
        log.debug("Request to update ActivityReport : {}", activityReport);
        return activityReportRepository.save(activityReport);
    }

    @Override
    public Optional<ActivityReport> partialUpdate(ActivityReport activityReport) {
        log.debug("Request to partially update ActivityReport : {}", activityReport);

        return activityReportRepository
            .findById(activityReport.getId())
            .map(existingActivityReport -> {
                if (activityReport.getMonth() != null) {
                    existingActivityReport.setMonth(activityReport.getMonth());
                }
                if (activityReport.getBalance() != null) {
                    existingActivityReport.setBalance(activityReport.getBalance());
                }
                if (activityReport.getComments() != null) {
                    existingActivityReport.setComments(activityReport.getComments());
                }

                return existingActivityReport;
            })
            .map(activityReportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityReport> findAll(Pageable pageable) {
        log.debug("Request to get all ActivityReports");
        return activityReportRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActivityReport> findOne(Long id) {
        log.debug("Request to get ActivityReport : {}", id);
        return activityReportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ActivityReport : {}", id);
        activityReportRepository.deleteById(id);
    }
}
