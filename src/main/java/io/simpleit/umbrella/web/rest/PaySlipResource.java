package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.PaySlip;
import io.simpleit.umbrella.repository.PaySlipRepository;
import io.simpleit.umbrella.service.PaySlipService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.PaySlip}.
 */
@RestController
@RequestMapping("/api/pay-slips")
public class PaySlipResource {

    private final Logger log = LoggerFactory.getLogger(PaySlipResource.class);

    private static final String ENTITY_NAME = "paySlip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaySlipService paySlipService;

    private final PaySlipRepository paySlipRepository;

    public PaySlipResource(PaySlipService paySlipService, PaySlipRepository paySlipRepository) {
        this.paySlipService = paySlipService;
        this.paySlipRepository = paySlipRepository;
    }

    /**
     * {@code POST  /pay-slips} : Create a new paySlip.
     *
     * @param paySlip the paySlip to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paySlip, or with status {@code 400 (Bad Request)} if the paySlip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaySlip> createPaySlip(@Valid @RequestBody PaySlip paySlip) throws URISyntaxException {
        log.debug("REST request to save PaySlip : {}", paySlip);
        if (paySlip.getId() != null) {
            throw new BadRequestAlertException("A new paySlip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paySlip = paySlipService.save(paySlip);
        return ResponseEntity.created(new URI("/api/pay-slips/" + paySlip.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paySlip.getId().toString()))
            .body(paySlip);
    }

    /**
     * {@code PUT  /pay-slips/:id} : Updates an existing paySlip.
     *
     * @param id the id of the paySlip to save.
     * @param paySlip the paySlip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paySlip,
     * or with status {@code 400 (Bad Request)} if the paySlip is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paySlip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaySlip> updatePaySlip(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaySlip paySlip
    ) throws URISyntaxException {
        log.debug("REST request to update PaySlip : {}, {}", id, paySlip);
        if (paySlip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paySlip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paySlipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paySlip = paySlipService.update(paySlip);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paySlip.getId().toString()))
            .body(paySlip);
    }

    /**
     * {@code PATCH  /pay-slips/:id} : Partial updates given fields of an existing paySlip, field will ignore if it is null
     *
     * @param id the id of the paySlip to save.
     * @param paySlip the paySlip to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paySlip,
     * or with status {@code 400 (Bad Request)} if the paySlip is not valid,
     * or with status {@code 404 (Not Found)} if the paySlip is not found,
     * or with status {@code 500 (Internal Server Error)} if the paySlip couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaySlip> partialUpdatePaySlip(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaySlip paySlip
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaySlip partially : {}, {}", id, paySlip);
        if (paySlip.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paySlip.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paySlipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaySlip> result = paySlipService.partialUpdate(paySlip);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paySlip.getId().toString())
        );
    }

    /**
     * {@code GET  /pay-slips} : get all the paySlips.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paySlips in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaySlip>> getAllPaySlips(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PaySlips");
        Page<PaySlip> page = paySlipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pay-slips/:id} : get the "id" paySlip.
     *
     * @param id the id of the paySlip to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paySlip, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaySlip> getPaySlip(@PathVariable("id") Long id) {
        log.debug("REST request to get PaySlip : {}", id);
        Optional<PaySlip> paySlip = paySlipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paySlip);
    }

    /**
     * {@code DELETE  /pay-slips/:id} : delete the "id" paySlip.
     *
     * @param id the id of the paySlip to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaySlip(@PathVariable("id") Long id) {
        log.debug("REST request to delete PaySlip : {}", id);
        paySlipService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
