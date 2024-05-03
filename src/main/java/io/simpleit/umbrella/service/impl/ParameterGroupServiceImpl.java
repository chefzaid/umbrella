package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.ParameterGroup;
import io.simpleit.umbrella.repository.ParameterGroupRepository;
import io.simpleit.umbrella.service.ParameterGroupService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.ParameterGroup}.
 */
@Service
@Transactional
public class ParameterGroupServiceImpl implements ParameterGroupService {

    private final Logger log = LoggerFactory.getLogger(ParameterGroupServiceImpl.class);

    private final ParameterGroupRepository parameterGroupRepository;

    public ParameterGroupServiceImpl(ParameterGroupRepository parameterGroupRepository) {
        this.parameterGroupRepository = parameterGroupRepository;
    }

    @Override
    public ParameterGroup save(ParameterGroup parameterGroup) {
        log.debug("Request to save ParameterGroup : {}", parameterGroup);
        return parameterGroupRepository.save(parameterGroup);
    }

    @Override
    public ParameterGroup update(ParameterGroup parameterGroup) {
        log.debug("Request to update ParameterGroup : {}", parameterGroup);
        return parameterGroupRepository.save(parameterGroup);
    }

    @Override
    public Optional<ParameterGroup> partialUpdate(ParameterGroup parameterGroup) {
        log.debug("Request to partially update ParameterGroup : {}", parameterGroup);

        return parameterGroupRepository
            .findById(parameterGroup.getId())
            .map(existingParameterGroup -> {
                if (parameterGroup.getLabel() != null) {
                    existingParameterGroup.setLabel(parameterGroup.getLabel());
                }
                if (parameterGroup.getDescription() != null) {
                    existingParameterGroup.setDescription(parameterGroup.getDescription());
                }

                return existingParameterGroup;
            })
            .map(parameterGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParameterGroup> findAll(Pageable pageable) {
        log.debug("Request to get all ParameterGroups");
        return parameterGroupRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParameterGroup> findOne(Long id) {
        log.debug("Request to get ParameterGroup : {}", id);
        return parameterGroupRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ParameterGroup : {}", id);
        parameterGroupRepository.deleteById(id);
    }
}
