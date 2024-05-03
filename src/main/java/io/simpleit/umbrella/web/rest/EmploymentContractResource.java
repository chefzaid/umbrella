package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.EmploymentContract;
import io.simpleit.umbrella.repository.EmploymentContractRepository;
import io.simpleit.umbrella.service.EmploymentContractService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.EmploymentContract}.
 */
@RestController
@RequestMapping("/api/employment-contracts")
public class EmploymentContractResource {

    private final Logger log = LoggerFactory.getLogger(EmploymentContractResource.class);

    private static final String ENTITY_NAME = "employmentContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmploymentContractService employmentContractService;

    private final EmploymentContractRepository employmentContractRepository;

    public EmploymentContractResource(
        EmploymentContractService employmentContractService,
        EmploymentContractRepository employmentContractRepository
    ) {
        this.employmentContractService = employmentContractService;
        this.employmentContractRepository = employmentContractRepository;
    }

    /**
     * {@code POST  /employment-contracts} : Create a new employmentContract.
     *
     * @param employmentContract the employmentContract to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employmentContract, or with status {@code 400 (Bad Request)} if the employmentContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmploymentContract> createEmploymentContract(@Valid @RequestBody EmploymentContract employmentContract)
        throws URISyntaxException {
        log.debug("REST request to save EmploymentContract : {}", employmentContract);
        if (employmentContract.getId() != null) {
            throw new BadRequestAlertException("A new employmentContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employmentContract = employmentContractService.save(employmentContract);
        return ResponseEntity.created(new URI("/api/employment-contracts/" + employmentContract.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employmentContract.getId().toString()))
            .body(employmentContract);
    }

    /**
     * {@code PUT  /employment-contracts/:id} : Updates an existing employmentContract.
     *
     * @param id the id of the employmentContract to save.
     * @param employmentContract the employmentContract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentContract,
     * or with status {@code 400 (Bad Request)} if the employmentContract is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employmentContract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmploymentContract> updateEmploymentContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmploymentContract employmentContract
    ) throws URISyntaxException {
        log.debug("REST request to update EmploymentContract : {}, {}", id, employmentContract);
        if (employmentContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentContract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employmentContract = employmentContractService.update(employmentContract);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentContract.getId().toString()))
            .body(employmentContract);
    }

    /**
     * {@code PATCH  /employment-contracts/:id} : Partial updates given fields of an existing employmentContract, field will ignore if it is null
     *
     * @param id the id of the employmentContract to save.
     * @param employmentContract the employmentContract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentContract,
     * or with status {@code 400 (Bad Request)} if the employmentContract is not valid,
     * or with status {@code 404 (Not Found)} if the employmentContract is not found,
     * or with status {@code 500 (Internal Server Error)} if the employmentContract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmploymentContract> partialUpdateEmploymentContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmploymentContract employmentContract
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmploymentContract partially : {}, {}", id, employmentContract);
        if (employmentContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentContract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmploymentContract> result = employmentContractService.partialUpdate(employmentContract);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentContract.getId().toString())
        );
    }

    /**
     * {@code GET  /employment-contracts} : get all the employmentContracts.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employmentContracts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmploymentContract>> getAllEmploymentContracts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("employee-is-null".equals(filter)) {
            log.debug("REST request to get all EmploymentContracts where employee is null");
            return new ResponseEntity<>(employmentContractService.findAllWhereEmployeeIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of EmploymentContracts");
        Page<EmploymentContract> page = employmentContractService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employment-contracts/:id} : get the "id" employmentContract.
     *
     * @param id the id of the employmentContract to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employmentContract, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmploymentContract> getEmploymentContract(@PathVariable("id") Long id) {
        log.debug("REST request to get EmploymentContract : {}", id);
        Optional<EmploymentContract> employmentContract = employmentContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employmentContract);
    }

    /**
     * {@code DELETE  /employment-contracts/:id} : delete the "id" employmentContract.
     *
     * @param id the id of the employmentContract to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploymentContract(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmploymentContract : {}", id);
        employmentContractService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
