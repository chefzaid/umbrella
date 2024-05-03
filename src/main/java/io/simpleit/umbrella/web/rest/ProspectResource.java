package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.Prospect;
import io.simpleit.umbrella.repository.ProspectRepository;
import io.simpleit.umbrella.service.ProspectService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.Prospect}.
 */
@RestController
@RequestMapping("/api/prospects")
public class ProspectResource {

    private final Logger log = LoggerFactory.getLogger(ProspectResource.class);

    private static final String ENTITY_NAME = "prospect";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProspectService prospectService;

    private final ProspectRepository prospectRepository;

    public ProspectResource(ProspectService prospectService, ProspectRepository prospectRepository) {
        this.prospectService = prospectService;
        this.prospectRepository = prospectRepository;
    }

    /**
     * {@code POST  /prospects} : Create a new prospect.
     *
     * @param prospect the prospect to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prospect, or with status {@code 400 (Bad Request)} if the prospect has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Prospect> createProspect(@Valid @RequestBody Prospect prospect) throws URISyntaxException {
        log.debug("REST request to save Prospect : {}", prospect);
        if (prospect.getId() != null) {
            throw new BadRequestAlertException("A new prospect cannot already have an ID", ENTITY_NAME, "idexists");
        }
        prospect = prospectService.save(prospect);
        return ResponseEntity.created(new URI("/api/prospects/" + prospect.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, prospect.getId().toString()))
            .body(prospect);
    }

    /**
     * {@code PUT  /prospects/:id} : Updates an existing prospect.
     *
     * @param id the id of the prospect to save.
     * @param prospect the prospect to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prospect,
     * or with status {@code 400 (Bad Request)} if the prospect is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prospect couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Prospect> updateProspect(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Prospect prospect
    ) throws URISyntaxException {
        log.debug("REST request to update Prospect : {}, {}", id, prospect);
        if (prospect.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prospect.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prospectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        prospect = prospectService.update(prospect);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prospect.getId().toString()))
            .body(prospect);
    }

    /**
     * {@code PATCH  /prospects/:id} : Partial updates given fields of an existing prospect, field will ignore if it is null
     *
     * @param id the id of the prospect to save.
     * @param prospect the prospect to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prospect,
     * or with status {@code 400 (Bad Request)} if the prospect is not valid,
     * or with status {@code 404 (Not Found)} if the prospect is not found,
     * or with status {@code 500 (Internal Server Error)} if the prospect couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Prospect> partialUpdateProspect(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Prospect prospect
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prospect partially : {}, {}", id, prospect);
        if (prospect.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prospect.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prospectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prospect> result = prospectService.partialUpdate(prospect);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prospect.getId().toString())
        );
    }

    /**
     * {@code GET  /prospects} : get all the prospects.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prospects in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Prospect>> getAllProspects(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Prospects");
        Page<Prospect> page = prospectService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prospects/:id} : get the "id" prospect.
     *
     * @param id the id of the prospect to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prospect, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Prospect> getProspect(@PathVariable("id") Long id) {
        log.debug("REST request to get Prospect : {}", id);
        Optional<Prospect> prospect = prospectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prospect);
    }

    /**
     * {@code DELETE  /prospects/:id} : delete the "id" prospect.
     *
     * @param id the id of the prospect to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProspect(@PathVariable("id") Long id) {
        log.debug("REST request to delete Prospect : {}", id);
        prospectService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
