package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.MileageAllowance;
import io.simpleit.umbrella.repository.MileageAllowanceRepository;
import io.simpleit.umbrella.service.MileageAllowanceService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.simpleit.umbrella.domain.MileageAllowance}.
 */
@RestController
@RequestMapping("/api/mileage-allowances")
public class MileageAllowanceResource {

    private final Logger log = LoggerFactory.getLogger(MileageAllowanceResource.class);

    private static final String ENTITY_NAME = "mileageAllowance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MileageAllowanceService mileageAllowanceService;

    private final MileageAllowanceRepository mileageAllowanceRepository;

    public MileageAllowanceResource(
        MileageAllowanceService mileageAllowanceService,
        MileageAllowanceRepository mileageAllowanceRepository
    ) {
        this.mileageAllowanceService = mileageAllowanceService;
        this.mileageAllowanceRepository = mileageAllowanceRepository;
    }

    /**
     * {@code POST  /mileage-allowances} : Create a new mileageAllowance.
     *
     * @param mileageAllowance the mileageAllowance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mileageAllowance, or with status {@code 400 (Bad Request)} if the mileageAllowance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MileageAllowance> createMileageAllowance(@Valid @RequestBody MileageAllowance mileageAllowance)
        throws URISyntaxException {
        log.debug("REST request to save MileageAllowance : {}", mileageAllowance);
        if (mileageAllowance.getId() != null) {
            throw new BadRequestAlertException("A new mileageAllowance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mileageAllowance = mileageAllowanceService.save(mileageAllowance);
        return ResponseEntity.created(new URI("/api/mileage-allowances/" + mileageAllowance.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mileageAllowance.getId().toString()))
            .body(mileageAllowance);
    }

    /**
     * {@code PUT  /mileage-allowances/:id} : Updates an existing mileageAllowance.
     *
     * @param id the id of the mileageAllowance to save.
     * @param mileageAllowance the mileageAllowance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mileageAllowance,
     * or with status {@code 400 (Bad Request)} if the mileageAllowance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mileageAllowance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MileageAllowance> updateMileageAllowance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MileageAllowance mileageAllowance
    ) throws URISyntaxException {
        log.debug("REST request to update MileageAllowance : {}, {}", id, mileageAllowance);
        if (mileageAllowance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mileageAllowance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mileageAllowanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mileageAllowance = mileageAllowanceService.update(mileageAllowance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mileageAllowance.getId().toString()))
            .body(mileageAllowance);
    }

    /**
     * {@code PATCH  /mileage-allowances/:id} : Partial updates given fields of an existing mileageAllowance, field will ignore if it is null
     *
     * @param id the id of the mileageAllowance to save.
     * @param mileageAllowance the mileageAllowance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mileageAllowance,
     * or with status {@code 400 (Bad Request)} if the mileageAllowance is not valid,
     * or with status {@code 404 (Not Found)} if the mileageAllowance is not found,
     * or with status {@code 500 (Internal Server Error)} if the mileageAllowance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MileageAllowance> partialUpdateMileageAllowance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MileageAllowance mileageAllowance
    ) throws URISyntaxException {
        log.debug("REST request to partial update MileageAllowance partially : {}, {}", id, mileageAllowance);
        if (mileageAllowance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mileageAllowance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mileageAllowanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MileageAllowance> result = mileageAllowanceService.partialUpdate(mileageAllowance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mileageAllowance.getId().toString())
        );
    }

    /**
     * {@code GET  /mileage-allowances} : get all the mileageAllowances.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mileageAllowances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MileageAllowance>> getAllMileageAllowances(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("expensenote-is-null".equals(filter)) {
            log.debug("REST request to get all MileageAllowances where expenseNote is null");
            return new ResponseEntity<>(mileageAllowanceService.findAllWhereExpenseNoteIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of MileageAllowances");
        Page<MileageAllowance> page = mileageAllowanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mileage-allowances/:id} : get the "id" mileageAllowance.
     *
     * @param id the id of the mileageAllowance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mileageAllowance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MileageAllowance> getMileageAllowance(@PathVariable("id") Long id) {
        log.debug("REST request to get MileageAllowance : {}", id);
        Optional<MileageAllowance> mileageAllowance = mileageAllowanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mileageAllowance);
    }

    /**
     * {@code DELETE  /mileage-allowances/:id} : delete the "id" mileageAllowance.
     *
     * @param id the id of the mileageAllowance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMileageAllowance(@PathVariable("id") Long id) {
        log.debug("REST request to delete MileageAllowance : {}", id);
        mileageAllowanceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
