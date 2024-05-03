package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.ActivityReport;
import io.simpleit.umbrella.repository.ActivityReportRepository;
import io.simpleit.umbrella.service.ActivityReportService;
import io.simpleit.umbrella.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.simpleit.umbrella.domain.ActivityReport}.
 */
@RestController
@RequestMapping("/api/activity-reports")
public class ActivityReportResource {

    private final Logger log = LoggerFactory.getLogger(ActivityReportResource.class);

    private static final String ENTITY_NAME = "activityReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityReportService activityReportService;

    private final ActivityReportRepository activityReportRepository;

    public ActivityReportResource(ActivityReportService activityReportService, ActivityReportRepository activityReportRepository) {
        this.activityReportService = activityReportService;
        this.activityReportRepository = activityReportRepository;
    }

    /**
     * {@code POST  /activity-reports} : Create a new activityReport.
     *
     * @param activityReport the activityReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityReport, or with status {@code 400 (Bad Request)} if the activityReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ActivityReport> createActivityReport(@Valid @RequestBody ActivityReport activityReport)
        throws URISyntaxException {
        log.debug("REST request to save ActivityReport : {}", activityReport);
        if (activityReport.getId() != null) {
            throw new BadRequestAlertException("A new activityReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activityReport = activityReportService.save(activityReport);
        return ResponseEntity.created(new URI("/api/activity-reports/" + activityReport.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, activityReport.getId().toString()))
            .body(activityReport);
    }

    /**
     * {@code PUT  /activity-reports/:id} : Updates an existing activityReport.
     *
     * @param id the id of the activityReport to save.
     * @param activityReport the activityReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityReport,
     * or with status {@code 400 (Bad Request)} if the activityReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActivityReport> updateActivityReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ActivityReport activityReport
    ) throws URISyntaxException {
        log.debug("REST request to update ActivityReport : {}, {}", id, activityReport);
        if (activityReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activityReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activityReport = activityReportService.update(activityReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activityReport.getId().toString()))
            .body(activityReport);
    }

    /**
     * {@code PATCH  /activity-reports/:id} : Partial updates given fields of an existing activityReport, field will ignore if it is null
     *
     * @param id the id of the activityReport to save.
     * @param activityReport the activityReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityReport,
     * or with status {@code 400 (Bad Request)} if the activityReport is not valid,
     * or with status {@code 404 (Not Found)} if the activityReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the activityReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActivityReport> partialUpdateActivityReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ActivityReport activityReport
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActivityReport partially : {}, {}", id, activityReport);
        if (activityReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activityReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activityReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActivityReport> result = activityReportService.partialUpdate(activityReport);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activityReport.getId().toString())
        );
    }

    /**
     * {@code GET  /activity-reports} : get all the activityReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ActivityReport>> getAllActivityReports(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ActivityReports");
        Page<ActivityReport> page = activityReportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /activity-reports/:id} : get the "id" activityReport.
     *
     * @param id the id of the activityReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActivityReport> getActivityReport(@PathVariable("id") Long id) {
        log.debug("REST request to get ActivityReport : {}", id);
        Optional<ActivityReport> activityReport = activityReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityReport);
    }

    /**
     * {@code DELETE  /activity-reports/:id} : delete the "id" activityReport.
     *
     * @param id the id of the activityReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivityReport(@PathVariable("id") Long id) {
        log.debug("REST request to delete ActivityReport : {}", id);
        activityReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
