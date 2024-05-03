package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.TimeSheetLine;
import io.simpleit.umbrella.repository.TimeSheetLineRepository;
import io.simpleit.umbrella.service.TimeSheetLineService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.TimeSheetLine}.
 */
@RestController
@RequestMapping("/api/time-sheet-lines")
public class TimeSheetLineResource {

    private final Logger log = LoggerFactory.getLogger(TimeSheetLineResource.class);

    private static final String ENTITY_NAME = "timeSheetLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeSheetLineService timeSheetLineService;

    private final TimeSheetLineRepository timeSheetLineRepository;

    public TimeSheetLineResource(TimeSheetLineService timeSheetLineService, TimeSheetLineRepository timeSheetLineRepository) {
        this.timeSheetLineService = timeSheetLineService;
        this.timeSheetLineRepository = timeSheetLineRepository;
    }

    /**
     * {@code POST  /time-sheet-lines} : Create a new timeSheetLine.
     *
     * @param timeSheetLine the timeSheetLine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeSheetLine, or with status {@code 400 (Bad Request)} if the timeSheetLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimeSheetLine> createTimeSheetLine(@Valid @RequestBody TimeSheetLine timeSheetLine) throws URISyntaxException {
        log.debug("REST request to save TimeSheetLine : {}", timeSheetLine);
        if (timeSheetLine.getId() != null) {
            throw new BadRequestAlertException("A new timeSheetLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timeSheetLine = timeSheetLineService.save(timeSheetLine);
        return ResponseEntity.created(new URI("/api/time-sheet-lines/" + timeSheetLine.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timeSheetLine.getId().toString()))
            .body(timeSheetLine);
    }

    /**
     * {@code PUT  /time-sheet-lines/:id} : Updates an existing timeSheetLine.
     *
     * @param id the id of the timeSheetLine to save.
     * @param timeSheetLine the timeSheetLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSheetLine,
     * or with status {@code 400 (Bad Request)} if the timeSheetLine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeSheetLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimeSheetLine> updateTimeSheetLine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimeSheetLine timeSheetLine
    ) throws URISyntaxException {
        log.debug("REST request to update TimeSheetLine : {}, {}", id, timeSheetLine);
        if (timeSheetLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeSheetLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeSheetLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timeSheetLine = timeSheetLineService.update(timeSheetLine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timeSheetLine.getId().toString()))
            .body(timeSheetLine);
    }

    /**
     * {@code PATCH  /time-sheet-lines/:id} : Partial updates given fields of an existing timeSheetLine, field will ignore if it is null
     *
     * @param id the id of the timeSheetLine to save.
     * @param timeSheetLine the timeSheetLine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeSheetLine,
     * or with status {@code 400 (Bad Request)} if the timeSheetLine is not valid,
     * or with status {@code 404 (Not Found)} if the timeSheetLine is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeSheetLine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimeSheetLine> partialUpdateTimeSheetLine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimeSheetLine timeSheetLine
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeSheetLine partially : {}, {}", id, timeSheetLine);
        if (timeSheetLine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeSheetLine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeSheetLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimeSheetLine> result = timeSheetLineService.partialUpdate(timeSheetLine);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timeSheetLine.getId().toString())
        );
    }

    /**
     * {@code GET  /time-sheet-lines} : get all the timeSheetLines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeSheetLines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TimeSheetLine>> getAllTimeSheetLines(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TimeSheetLines");
        Page<TimeSheetLine> page = timeSheetLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /time-sheet-lines/:id} : get the "id" timeSheetLine.
     *
     * @param id the id of the timeSheetLine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeSheetLine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimeSheetLine> getTimeSheetLine(@PathVariable("id") Long id) {
        log.debug("REST request to get TimeSheetLine : {}", id);
        Optional<TimeSheetLine> timeSheetLine = timeSheetLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeSheetLine);
    }

    /**
     * {@code DELETE  /time-sheet-lines/:id} : delete the "id" timeSheetLine.
     *
     * @param id the id of the timeSheetLine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSheetLine(@PathVariable("id") Long id) {
        log.debug("REST request to delete TimeSheetLine : {}", id);
        timeSheetLineService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
