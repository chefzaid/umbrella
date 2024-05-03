package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.Formula;
import io.simpleit.umbrella.repository.FormulaRepository;
import io.simpleit.umbrella.service.FormulaService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.Formula}.
 */
@RestController
@RequestMapping("/api/formulas")
public class FormulaResource {

    private final Logger log = LoggerFactory.getLogger(FormulaResource.class);

    private static final String ENTITY_NAME = "formula";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormulaService formulaService;

    private final FormulaRepository formulaRepository;

    public FormulaResource(FormulaService formulaService, FormulaRepository formulaRepository) {
        this.formulaService = formulaService;
        this.formulaRepository = formulaRepository;
    }

    /**
     * {@code POST  /formulas} : Create a new formula.
     *
     * @param formula the formula to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formula, or with status {@code 400 (Bad Request)} if the formula has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Formula> createFormula(@Valid @RequestBody Formula formula) throws URISyntaxException {
        log.debug("REST request to save Formula : {}", formula);
        if (formula.getId() != null) {
            throw new BadRequestAlertException("A new formula cannot already have an ID", ENTITY_NAME, "idexists");
        }
        formula = formulaService.save(formula);
        return ResponseEntity.created(new URI("/api/formulas/" + formula.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, formula.getId().toString()))
            .body(formula);
    }

    /**
     * {@code PUT  /formulas/:id} : Updates an existing formula.
     *
     * @param id the id of the formula to save.
     * @param formula the formula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formula,
     * or with status {@code 400 (Bad Request)} if the formula is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Formula> updateFormula(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Formula formula
    ) throws URISyntaxException {
        log.debug("REST request to update Formula : {}, {}", id, formula);
        if (formula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        formula = formulaService.update(formula);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formula.getId().toString()))
            .body(formula);
    }

    /**
     * {@code PATCH  /formulas/:id} : Partial updates given fields of an existing formula, field will ignore if it is null
     *
     * @param id the id of the formula to save.
     * @param formula the formula to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formula,
     * or with status {@code 400 (Bad Request)} if the formula is not valid,
     * or with status {@code 404 (Not Found)} if the formula is not found,
     * or with status {@code 500 (Internal Server Error)} if the formula couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Formula> partialUpdateFormula(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Formula formula
    ) throws URISyntaxException {
        log.debug("REST request to partial update Formula partially : {}, {}", id, formula);
        if (formula.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formula.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formulaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Formula> result = formulaService.partialUpdate(formula);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formula.getId().toString())
        );
    }

    /**
     * {@code GET  /formulas} : get all the formulas.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formulas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Formula>> getAllFormulas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("prospect-is-null".equals(filter)) {
            log.debug("REST request to get all Formulas where prospect is null");
            return new ResponseEntity<>(formulaService.findAllWhereProspectIsNull(), HttpStatus.OK);
        }

        if ("employmentcontract-is-null".equals(filter)) {
            log.debug("REST request to get all Formulas where employmentContract is null");
            return new ResponseEntity<>(formulaService.findAllWhereEmploymentContractIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Formulas");
        Page<Formula> page = formulaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formulas/:id} : get the "id" formula.
     *
     * @param id the id of the formula to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formula, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Formula> getFormula(@PathVariable("id") Long id) {
        log.debug("REST request to get Formula : {}", id);
        Optional<Formula> formula = formulaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formula);
    }

    /**
     * {@code DELETE  /formulas/:id} : delete the "id" formula.
     *
     * @param id the id of the formula to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormula(@PathVariable("id") Long id) {
        log.debug("REST request to delete Formula : {}", id);
        formulaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
