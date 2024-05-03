package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.IdDocument;
import io.simpleit.umbrella.repository.IdDocumentRepository;
import io.simpleit.umbrella.service.IdDocumentService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.IdDocument}.
 */
@RestController
@RequestMapping("/api/id-documents")
public class IdDocumentResource {

    private final Logger log = LoggerFactory.getLogger(IdDocumentResource.class);

    private static final String ENTITY_NAME = "idDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdDocumentService idDocumentService;

    private final IdDocumentRepository idDocumentRepository;

    public IdDocumentResource(IdDocumentService idDocumentService, IdDocumentRepository idDocumentRepository) {
        this.idDocumentService = idDocumentService;
        this.idDocumentRepository = idDocumentRepository;
    }

    /**
     * {@code POST  /id-documents} : Create a new idDocument.
     *
     * @param idDocument the idDocument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new idDocument, or with status {@code 400 (Bad Request)} if the idDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IdDocument> createIdDocument(@Valid @RequestBody IdDocument idDocument) throws URISyntaxException {
        log.debug("REST request to save IdDocument : {}", idDocument);
        if (idDocument.getId() != null) {
            throw new BadRequestAlertException("A new idDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        idDocument = idDocumentService.save(idDocument);
        return ResponseEntity.created(new URI("/api/id-documents/" + idDocument.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, idDocument.getId().toString()))
            .body(idDocument);
    }

    /**
     * {@code PUT  /id-documents/:id} : Updates an existing idDocument.
     *
     * @param id the id of the idDocument to save.
     * @param idDocument the idDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idDocument,
     * or with status {@code 400 (Bad Request)} if the idDocument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the idDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdDocument> updateIdDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdDocument idDocument
    ) throws URISyntaxException {
        log.debug("REST request to update IdDocument : {}, {}", id, idDocument);
        if (idDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, idDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!idDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        idDocument = idDocumentService.update(idDocument);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idDocument.getId().toString()))
            .body(idDocument);
    }

    /**
     * {@code PATCH  /id-documents/:id} : Partial updates given fields of an existing idDocument, field will ignore if it is null
     *
     * @param id the id of the idDocument to save.
     * @param idDocument the idDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idDocument,
     * or with status {@code 400 (Bad Request)} if the idDocument is not valid,
     * or with status {@code 404 (Not Found)} if the idDocument is not found,
     * or with status {@code 500 (Internal Server Error)} if the idDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IdDocument> partialUpdateIdDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdDocument idDocument
    ) throws URISyntaxException {
        log.debug("REST request to partial update IdDocument partially : {}, {}", id, idDocument);
        if (idDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, idDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!idDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdDocument> result = idDocumentService.partialUpdate(idDocument);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idDocument.getId().toString())
        );
    }

    /**
     * {@code GET  /id-documents} : get all the idDocuments.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of idDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IdDocument>> getAllIdDocuments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("employee-is-null".equals(filter)) {
            log.debug("REST request to get all IdDocuments where employee is null");
            return new ResponseEntity<>(idDocumentService.findAllWhereEmployeeIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of IdDocuments");
        Page<IdDocument> page = idDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /id-documents/:id} : get the "id" idDocument.
     *
     * @param id the id of the idDocument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the idDocument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdDocument> getIdDocument(@PathVariable("id") Long id) {
        log.debug("REST request to get IdDocument : {}", id);
        Optional<IdDocument> idDocument = idDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(idDocument);
    }

    /**
     * {@code DELETE  /id-documents/:id} : delete the "id" idDocument.
     *
     * @param id the id of the idDocument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdDocument(@PathVariable("id") Long id) {
        log.debug("REST request to delete IdDocument : {}", id);
        idDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
