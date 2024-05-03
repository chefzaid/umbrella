package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.ExpenseNote;
import io.simpleit.umbrella.repository.ExpenseNoteRepository;
import io.simpleit.umbrella.service.ExpenseNoteService;
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
 * REST controller for managing {@link io.simpleit.umbrella.domain.ExpenseNote}.
 */
@RestController
@RequestMapping("/api/expense-notes")
public class ExpenseNoteResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseNoteResource.class);

    private static final String ENTITY_NAME = "expenseNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseNoteService expenseNoteService;

    private final ExpenseNoteRepository expenseNoteRepository;

    public ExpenseNoteResource(ExpenseNoteService expenseNoteService, ExpenseNoteRepository expenseNoteRepository) {
        this.expenseNoteService = expenseNoteService;
        this.expenseNoteRepository = expenseNoteRepository;
    }

    /**
     * {@code POST  /expense-notes} : Create a new expenseNote.
     *
     * @param expenseNote the expenseNote to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseNote, or with status {@code 400 (Bad Request)} if the expenseNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExpenseNote> createExpenseNote(@Valid @RequestBody ExpenseNote expenseNote) throws URISyntaxException {
        log.debug("REST request to save ExpenseNote : {}", expenseNote);
        if (expenseNote.getId() != null) {
            throw new BadRequestAlertException("A new expenseNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        expenseNote = expenseNoteService.save(expenseNote);
        return ResponseEntity.created(new URI("/api/expense-notes/" + expenseNote.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, expenseNote.getId().toString()))
            .body(expenseNote);
    }

    /**
     * {@code PUT  /expense-notes/:id} : Updates an existing expenseNote.
     *
     * @param id the id of the expenseNote to save.
     * @param expenseNote the expenseNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseNote,
     * or with status {@code 400 (Bad Request)} if the expenseNote is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseNote> updateExpenseNote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExpenseNote expenseNote
    ) throws URISyntaxException {
        log.debug("REST request to update ExpenseNote : {}, {}", id, expenseNote);
        if (expenseNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseNote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        expenseNote = expenseNoteService.update(expenseNote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expenseNote.getId().toString()))
            .body(expenseNote);
    }

    /**
     * {@code PATCH  /expense-notes/:id} : Partial updates given fields of an existing expenseNote, field will ignore if it is null
     *
     * @param id the id of the expenseNote to save.
     * @param expenseNote the expenseNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseNote,
     * or with status {@code 400 (Bad Request)} if the expenseNote is not valid,
     * or with status {@code 404 (Not Found)} if the expenseNote is not found,
     * or with status {@code 500 (Internal Server Error)} if the expenseNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpenseNote> partialUpdateExpenseNote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExpenseNote expenseNote
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExpenseNote partially : {}, {}", id, expenseNote);
        if (expenseNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseNote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpenseNote> result = expenseNoteService.partialUpdate(expenseNote);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expenseNote.getId().toString())
        );
    }

    /**
     * {@code GET  /expense-notes} : get all the expenseNotes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseNotes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExpenseNote>> getAllExpenseNotes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ExpenseNotes");
        Page<ExpenseNote> page = expenseNoteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expense-notes/:id} : get the "id" expenseNote.
     *
     * @param id the id of the expenseNote to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseNote, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseNote> getExpenseNote(@PathVariable("id") Long id) {
        log.debug("REST request to get ExpenseNote : {}", id);
        Optional<ExpenseNote> expenseNote = expenseNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expenseNote);
    }

    /**
     * {@code DELETE  /expense-notes/:id} : delete the "id" expenseNote.
     *
     * @param id the id of the expenseNote to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseNote(@PathVariable("id") Long id) {
        log.debug("REST request to delete ExpenseNote : {}", id);
        expenseNoteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
