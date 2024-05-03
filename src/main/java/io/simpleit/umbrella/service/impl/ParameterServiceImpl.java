package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Parameter;
import io.simpleit.umbrella.repository.ParameterRepository;
import io.simpleit.umbrella.service.ParameterService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Parameter}.
 */
@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    private final Logger log = LoggerFactory.getLogger(ParameterServiceImpl.class);

    private final ParameterRepository parameterRepository;

    public ParameterServiceImpl(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    @Override
    public Parameter save(Parameter parameter) {
        log.debug("Request to save Parameter : {}", parameter);
        return parameterRepository.save(parameter);
    }

    @Override
    public Parameter update(Parameter parameter) {
        log.debug("Request to update Parameter : {}", parameter);
        return parameterRepository.save(parameter);
    }

    @Override
    public Optional<Parameter> partialUpdate(Parameter parameter) {
        log.debug("Request to partially update Parameter : {}", parameter);

        return parameterRepository
            .findById(parameter.getId())
            .map(existingParameter -> {
                if (parameter.getLabel() != null) {
                    existingParameter.setLabel(parameter.getLabel());
                }
                if (parameter.getValue() != null) {
                    existingParameter.setValue(parameter.getValue());
                }

                return existingParameter;
            })
            .map(parameterRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Parameter> findAll(Pageable pageable) {
        log.debug("Request to get all Parameters");
        return parameterRepository.findAll(pageable);
    }

    /**
     *  Get all the parameters where Document is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Parameter> findAllWhereDocumentIsNull() {
        log.debug("Request to get all parameters where Document is null");
        return StreamSupport.stream(parameterRepository.findAll().spliterator(), false)
            .filter(parameter -> parameter.getDocument() == null)
            .toList();
    }

    /**
     *  Get all the parameters where Expense is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Parameter> findAllWhereExpenseIsNull() {
        log.debug("Request to get all parameters where Expense is null");
        return StreamSupport.stream(parameterRepository.findAll().spliterator(), false)
            .filter(parameter -> parameter.getExpense() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Parameter> findOne(Long id) {
        log.debug("Request to get Parameter : {}", id);
        return parameterRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Parameter : {}", id);
        parameterRepository.deleteById(id);
    }
}
