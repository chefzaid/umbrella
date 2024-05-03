package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Operation;
import io.simpleit.umbrella.repository.OperationRepository;
import io.simpleit.umbrella.service.OperationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Operation}.
 */
@Service
@Transactional
public class OperationServiceImpl implements OperationService {

    private final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;

    public OperationServiceImpl(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Override
    public Operation save(Operation operation) {
        log.debug("Request to save Operation : {}", operation);
        return operationRepository.save(operation);
    }

    @Override
    public Operation update(Operation operation) {
        log.debug("Request to update Operation : {}", operation);
        return operationRepository.save(operation);
    }

    @Override
    public Optional<Operation> partialUpdate(Operation operation) {
        log.debug("Request to partially update Operation : {}", operation);

        return operationRepository
            .findById(operation.getId())
            .map(existingOperation -> {
                if (operation.getDescription() != null) {
                    existingOperation.setDescription(operation.getDescription());
                }
                if (operation.getAmount() != null) {
                    existingOperation.setAmount(operation.getAmount());
                }
                if (operation.getOperationType() != null) {
                    existingOperation.setOperationType(operation.getOperationType());
                }

                return existingOperation;
            })
            .map(operationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Operation> findAll(Pageable pageable) {
        log.debug("Request to get all Operations");
        return operationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Operation> findOne(Long id) {
        log.debug("Request to get Operation : {}", id);
        return operationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Operation : {}", id);
        operationRepository.deleteById(id);
    }
}
