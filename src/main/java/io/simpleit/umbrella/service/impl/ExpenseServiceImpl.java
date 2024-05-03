package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Expense;
import io.simpleit.umbrella.repository.ExpenseRepository;
import io.simpleit.umbrella.service.ExpenseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Expense}.
 */
@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense save(Expense expense) {
        log.debug("Request to save Expense : {}", expense);
        return expenseRepository.save(expense);
    }

    @Override
    public Expense update(Expense expense) {
        log.debug("Request to update Expense : {}", expense);
        return expenseRepository.save(expense);
    }

    @Override
    public Optional<Expense> partialUpdate(Expense expense) {
        log.debug("Request to partially update Expense : {}", expense);

        return expenseRepository
            .findById(expense.getId())
            .map(existingExpense -> {
                if (expense.getLabel() != null) {
                    existingExpense.setLabel(expense.getLabel());
                }
                if (expense.getDescription() != null) {
                    existingExpense.setDescription(expense.getDescription());
                }
                if (expense.getAmount() != null) {
                    existingExpense.setAmount(expense.getAmount());
                }
                if (expense.getCurrency() != null) {
                    existingExpense.setCurrency(expense.getCurrency());
                }
                if (expense.getTax() != null) {
                    existingExpense.setTax(expense.getTax());
                }
                if (expense.getExpenseDate() != null) {
                    existingExpense.setExpenseDate(expense.getExpenseDate());
                }
                if (expense.getRebillableToClient() != null) {
                    existingExpense.setRebillableToClient(expense.getRebillableToClient());
                }
                if (expense.getComment() != null) {
                    existingExpense.setComment(expense.getComment());
                }
                if (expense.getSubmitDate() != null) {
                    existingExpense.setSubmitDate(expense.getSubmitDate());
                }
                if (expense.getValidationDate() != null) {
                    existingExpense.setValidationDate(expense.getValidationDate());
                }

                return existingExpense;
            })
            .map(expenseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Expense> findAll(Pageable pageable) {
        log.debug("Request to get all Expenses");
        return expenseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Expense> findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
        return expenseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Expense : {}", id);
        expenseRepository.deleteById(id);
    }
}
