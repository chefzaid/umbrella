package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.ServiceContract;
import io.simpleit.umbrella.repository.ServiceContractRepository;
import io.simpleit.umbrella.service.ServiceContractService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.ServiceContract}.
 */
@Service
@Transactional
public class ServiceContractServiceImpl implements ServiceContractService {

    private final Logger log = LoggerFactory.getLogger(ServiceContractServiceImpl.class);

    private final ServiceContractRepository serviceContractRepository;

    public ServiceContractServiceImpl(ServiceContractRepository serviceContractRepository) {
        this.serviceContractRepository = serviceContractRepository;
    }

    @Override
    public ServiceContract save(ServiceContract serviceContract) {
        log.debug("Request to save ServiceContract : {}", serviceContract);
        return serviceContractRepository.save(serviceContract);
    }

    @Override
    public ServiceContract update(ServiceContract serviceContract) {
        log.debug("Request to update ServiceContract : {}", serviceContract);
        return serviceContractRepository.save(serviceContract);
    }

    @Override
    public Optional<ServiceContract> partialUpdate(ServiceContract serviceContract) {
        log.debug("Request to partially update ServiceContract : {}", serviceContract);

        return serviceContractRepository
            .findById(serviceContract.getId())
            .map(existingServiceContract -> {
                if (serviceContract.getServiceLabel() != null) {
                    existingServiceContract.setServiceLabel(serviceContract.getServiceLabel());
                }
                if (serviceContract.getDailyRate() != null) {
                    existingServiceContract.setDailyRate(serviceContract.getDailyRate());
                }
                if (serviceContract.getStartDate() != null) {
                    existingServiceContract.setStartDate(serviceContract.getStartDate());
                }
                if (serviceContract.getEndDate() != null) {
                    existingServiceContract.setEndDate(serviceContract.getEndDate());
                }
                if (serviceContract.getExtensionTerms() != null) {
                    existingServiceContract.setExtensionTerms(serviceContract.getExtensionTerms());
                }
                if (serviceContract.getSignedBySupplier() != null) {
                    existingServiceContract.setSignedBySupplier(serviceContract.getSignedBySupplier());
                }
                if (serviceContract.getSignedByClient() != null) {
                    existingServiceContract.setSignedByClient(serviceContract.getSignedByClient());
                }

                return existingServiceContract;
            })
            .map(serviceContractRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceContract> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceContracts");
        return serviceContractRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceContract> findOne(Long id) {
        log.debug("Request to get ServiceContract : {}", id);
        return serviceContractRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServiceContract : {}", id);
        serviceContractRepository.deleteById(id);
    }
}
