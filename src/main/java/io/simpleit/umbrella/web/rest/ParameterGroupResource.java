package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.ParameterGroup;
import io.simpleit.umbrella.repository.ParameterGroupRepository;
import io.simpleit.umbrella.service.ParameterGroupService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.ParameterGroup}.
 */
@RestController
@RequestMapping("/api/parameter-groups")
public class ParameterGroupResource {

    private final Logger log = LoggerFactory.getLogger(ParameterGroupResource.class);

    private static final String ENTITY_NAME = "parameterGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParameterGroupService parameterGroupService;

    private final ParameterGroupRepository parameterGroupRepository;

    public ParameterGroupResource(ParameterGroupService parameterGroupService, ParameterGroupRepository parameterGroupRepository) {
        this.parameterGroupService = parameterGroupService;
        this.parameterGroupRepository = parameterGroupRepository;
    }

    /**
     * {@code POST  /parameter-groups} : Create a new parameterGroup.
     *
     * @param parameterGroup the parameterGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parameterGroup, or with status {@code 400 (Bad Request)} if the parameterGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ParameterGroup> createParameterGroup(@Valid @RequestBody ParameterGroup parameterGroup)
        throws URISyntaxException {
        log.debug("REST request to save ParameterGroup : {}", parameterGroup);
        if (parameterGroup.getId() != null) {
            throw new BadRequestAlertException("A new parameterGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        parameterGroup = parameterGroupService.save(parameterGroup);
        return ResponseEntity.created(new URI("/api/parameter-groups/" + parameterGroup.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, parameterGroup.getId().toString()))
            .body(parameterGroup);
    }

    /**
     * {@code PUT  /parameter-groups/:id} : Updates an existing parameterGroup.
     *
     * @param id the id of the parameterGroup to save.
     * @param parameterGroup the parameterGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parameterGroup,
     * or with status {@code 400 (Bad Request)} if the parameterGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parameterGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ParameterGroup> updateParameterGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParameterGroup parameterGroup
    ) throws URISyntaxException {
        log.debug("REST request to update ParameterGroup : {}, {}", id, parameterGroup);
        if (parameterGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parameterGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parameterGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        parameterGroup = parameterGroupService.update(parameterGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parameterGroup.getId().toString()))
            .body(parameterGroup);
    }

    /**
     * {@code PATCH  /parameter-groups/:id} : Partial updates given fields of an existing parameterGroup, field will ignore if it is null
     *
     * @param id the id of the parameterGroup to save.
     * @param parameterGroup the parameterGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parameterGroup,
     * or with status {@code 400 (Bad Request)} if the parameterGroup is not valid,
     * or with status {@code 404 (Not Found)} if the parameterGroup is not found,
     * or with status {@code 500 (Internal Server Error)} if the parameterGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParameterGroup> partialUpdateParameterGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParameterGroup parameterGroup
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParameterGroup partially : {}, {}", id, parameterGroup);
        if (parameterGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parameterGroup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parameterGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParameterGroup> result = parameterGroupService.partialUpdate(parameterGroup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parameterGroup.getId().toString())
        );
    }

    /**
     * {@code GET  /parameter-groups} : get all the parameterGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parameterGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ParameterGroup>> getAllParameterGroups(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ParameterGroups");
        Page<ParameterGroup> page = parameterGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parameter-groups/:id} : get the "id" parameterGroup.
     *
     * @param id the id of the parameterGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parameterGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParameterGroup> getParameterGroup(@PathVariable("id") Long id) {
        log.debug("REST request to get ParameterGroup : {}", id);
        Optional<ParameterGroup> parameterGroup = parameterGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parameterGroup);
    }

    /**
     * {@code DELETE  /parameter-groups/:id} : delete the "id" parameterGroup.
     *
     * @param id the id of the parameterGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParameterGroup(@PathVariable("id") Long id) {
        log.debug("REST request to delete ParameterGroup : {}", id);
        parameterGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
