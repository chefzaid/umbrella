package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.ExpenseType;
import io.simpleit.umbrella.repository.ExpenseTypeRepository;
import io.simpleit.umbrella.service.ExpenseTypeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.ExpenseType}.
 */
@Service
@Transactional
public class ExpenseTypeServiceImpl implements ExpenseTypeService {

    private final Logger log = LoggerFactory.getLogger(ExpenseTypeServiceImpl.class);

    private final ExpenseTypeRepository expenseTypeRepository;

    public ExpenseTypeServiceImpl(ExpenseTypeRepository expenseTypeRepository) {
        this.expenseTypeRepository = expenseTypeRepository;
    }

    @Override
    public ExpenseType save(ExpenseType expenseType) {
        log.debug("Request to save ExpenseType : {}", expenseType);
        return expenseTypeRepository.save(expenseType);
    }

    @Override
    public ExpenseType update(ExpenseType expenseType) {
        log.debug("Request to update ExpenseType : {}", expenseType);
        return expenseTypeRepository.save(expenseType);
    }

    @Override
    public Optional<ExpenseType> partialUpdate(ExpenseType expenseType) {
        log.debug("Request to partially update ExpenseType : {}", expenseType);

        return expenseTypeRepository
            .findById(expenseType.getId())
            .map(existingExpenseType -> {
                if (expenseType.getLabel() != null) {
                    existingExpenseType.setLabel(expenseType.getLabel());
                }
                if (expenseType.getDescription() != null) {
                    existingExpenseType.setDescription(expenseType.getDescription());
                }
                if (expenseType.getReimbursmentPct() != null) {
                    existingExpenseType.setReimbursmentPct(expenseType.getReimbursmentPct());
                }

                return existingExpenseType;
            })
            .map(expenseTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseType> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseTypes");
        return expenseTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExpenseType> findOne(Long id) {
        log.debug("Request to get ExpenseType : {}", id);
        return expenseTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExpenseType : {}", id);
        expenseTypeRepository.deleteById(id);
    }
}
