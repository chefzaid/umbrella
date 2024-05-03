package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.ServiceContract;
import io.simpleit.umbrella.repository.ServiceContractRepository;
import io.simpleit.umbrella.service.ServiceContractService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.ServiceContract}.
 */
@RestController
@RequestMapping("/api/service-contracts")
public class ServiceContractResource {

    private final Logger log = LoggerFactory.getLogger(ServiceContractResource.class);

    private static final String ENTITY_NAME = "serviceContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceContractService serviceContractService;

    private final ServiceContractRepository serviceContractRepository;

    public ServiceContractResource(ServiceContractService serviceContractService, ServiceContractRepository serviceContractRepository) {
        this.serviceContractService = serviceContractService;
        this.serviceContractRepository = serviceContractRepository;
    }

    /**
     * {@code POST  /service-contracts} : Create a new serviceContract.
     *
     * @param serviceContract the serviceContract to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceContract, or with status {@code 400 (Bad Request)} if the serviceContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceContract> createServiceContract(@Valid @RequestBody ServiceContract serviceContract)
        throws URISyntaxException {
        log.debug("REST request to save ServiceContract : {}", serviceContract);
        if (serviceContract.getId() != null) {
            throw new BadRequestAlertException("A new serviceContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceContract = serviceContractService.save(serviceContract);
        return ResponseEntity.created(new URI("/api/service-contracts/" + serviceContract.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceContract.getId().toString()))
            .body(serviceContract);
    }

    /**
     * {@code PUT  /service-contracts/:id} : Updates an existing serviceContract.
     *
     * @param id the id of the serviceContract to save.
     * @param serviceContract the serviceContract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceContract,
     * or with status {@code 400 (Bad Request)} if the serviceContract is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceContract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceContract> updateServiceContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceContract serviceContract
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceContract : {}, {}", id, serviceContract);
        if (serviceContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceContract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceContract = serviceContractService.update(serviceContract);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceContract.getId().toString()))
            .body(serviceContract);
    }

    /**
     * {@code PATCH  /service-contracts/:id} : Partial updates given fields of an existing serviceContract, field will ignore if it is null
     *
     * @param id the id of the serviceContract to save.
     * @param serviceContract the serviceContract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceContract,
     * or with status {@code 400 (Bad Request)} if the serviceContract is not valid,
     * or with status {@code 404 (Not Found)} if the serviceContract is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceContract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceContract> partialUpdateServiceContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceContract serviceContract
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceContract partially : {}, {}", id, serviceContract);
        if (serviceContract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceContract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceContract> result = serviceContractService.partialUpdate(serviceContract);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceContract.getId().toString())
        );
    }

    /**
     * {@code GET  /service-contracts} : get all the serviceContracts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceContracts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceContract>> getAllServiceContracts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ServiceContracts");
        Page<ServiceContract> page = serviceContractService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-contracts/:id} : get the "id" serviceContract.
     *
     * @param id the id of the serviceContract to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceContract, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceContract> getServiceContract(@PathVariable("id") Long id) {
        log.debug("REST request to get ServiceContract : {}", id);
        Optional<ServiceContract> serviceContract = serviceContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceContract);
    }

    /**
     * {@code DELETE  /service-contracts/:id} : delete the "id" serviceContract.
     *
     * @param id the id of the serviceContract to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceContract(@PathVariable("id") Long id) {
        log.debug("REST request to delete ServiceContract : {}", id);
        serviceContractService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
