package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.ExpenseNote;
import io.simpleit.umbrella.repository.ExpenseNoteRepository;
import io.simpleit.umbrella.service.ExpenseNoteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.ExpenseNote}.
 */
@Service
@Transactional
public class ExpenseNoteServiceImpl implements ExpenseNoteService {

    private final Logger log = LoggerFactory.getLogger(ExpenseNoteServiceImpl.class);

    private final ExpenseNoteRepository expenseNoteRepository;

    public ExpenseNoteServiceImpl(ExpenseNoteRepository expenseNoteRepository) {
        this.expenseNoteRepository = expenseNoteRepository;
    }

    @Override
    public ExpenseNote save(ExpenseNote expenseNote) {
        log.debug("Request to save ExpenseNote : {}", expenseNote);
        return expenseNoteRepository.save(expenseNote);
    }

    @Override
    public ExpenseNote update(ExpenseNote expenseNote) {
        log.debug("Request to update ExpenseNote : {}", expenseNote);
        return expenseNoteRepository.save(expenseNote);
    }

    @Override
    public Optional<ExpenseNote> partialUpdate(ExpenseNote expenseNote) {
        log.debug("Request to partially update ExpenseNote : {}", expenseNote);

        return expenseNoteRepository
            .findById(expenseNote.getId())
            .map(existingExpenseNote -> {
                if (expenseNote.getLabel() != null) {
                    existingExpenseNote.setLabel(expenseNote.getLabel());
                }
                if (expenseNote.getConcernedMonth() != null) {
                    existingExpenseNote.setConcernedMonth(expenseNote.getConcernedMonth());
                }
                if (expenseNote.getCreationDate() != null) {
                    existingExpenseNote.setCreationDate(expenseNote.getCreationDate());
                }
                if (expenseNote.getSubmitDate() != null) {
                    existingExpenseNote.setSubmitDate(expenseNote.getSubmitDate());
                }
                if (expenseNote.getValidationDate() != null) {
                    existingExpenseNote.setValidationDate(expenseNote.getValidationDate());
                }

                return existingExpenseNote;
            })
            .map(expenseNoteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseNote> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseNotes");
        return expenseNoteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExpenseNote> findOne(Long id) {
        log.debug("Request to get ExpenseNote : {}", id);
        return expenseNoteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExpenseNote : {}", id);
        expenseNoteRepository.deleteById(id);
    }
}
