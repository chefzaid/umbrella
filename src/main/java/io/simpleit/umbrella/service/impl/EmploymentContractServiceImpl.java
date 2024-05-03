package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.EmploymentContract;
import io.simpleit.umbrella.repository.EmploymentContractRepository;
import io.simpleit.umbrella.service.EmploymentContractService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.EmploymentContract}.
 */
@Service
@Transactional
public class EmploymentContractServiceImpl implements EmploymentContractService {

    private final Logger log = LoggerFactory.getLogger(EmploymentContractServiceImpl.class);

    private final EmploymentContractRepository employmentContractRepository;

    public EmploymentContractServiceImpl(EmploymentContractRepository employmentContractRepository) {
        this.employmentContractRepository = employmentContractRepository;
    }

    @Override
    public EmploymentContract save(EmploymentContract employmentContract) {
        log.debug("Request to save EmploymentContract : {}", employmentContract);
        return employmentContractRepository.save(employmentContract);
    }

    @Override
    public EmploymentContract update(EmploymentContract employmentContract) {
        log.debug("Request to update EmploymentContract : {}", employmentContract);
        return employmentContractRepository.save(employmentContract);
    }

    @Override
    public Optional<EmploymentContract> partialUpdate(EmploymentContract employmentContract) {
        log.debug("Request to partially update EmploymentContract : {}", employmentContract);

        return employmentContractRepository
            .findById(employmentContract.getId())
            .map(existingEmploymentContract -> {
                if (employmentContract.getType() != null) {
                    existingEmploymentContract.setType(employmentContract.getType());
                }
                if (employmentContract.getJobTitle() != null) {
                    existingEmploymentContract.setJobTitle(employmentContract.getJobTitle());
                }
                if (employmentContract.getHireDate() != null) {
                    existingEmploymentContract.setHireDate(employmentContract.getHireDate());
                }
                if (employmentContract.getSalary() != null) {
                    existingEmploymentContract.setSalary(employmentContract.getSalary());
                }
                if (employmentContract.getYearlyWorkDays() != null) {
                    existingEmploymentContract.setYearlyWorkDays(employmentContract.getYearlyWorkDays());
                }
                if (employmentContract.getSignedByEmployer() != null) {
                    existingEmploymentContract.setSignedByEmployer(employmentContract.getSignedByEmployer());
                }
                if (employmentContract.getSignedByEmployee() != null) {
                    existingEmploymentContract.setSignedByEmployee(employmentContract.getSignedByEmployee());
                }

                return existingEmploymentContract;
            })
            .map(employmentContractRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmploymentContract> findAll(Pageable pageable) {
        log.debug("Request to get all EmploymentContracts");
        return employmentContractRepository.findAll(pageable);
    }

    /**
     *  Get all the employmentContracts where Employee is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EmploymentContract> findAllWhereEmployeeIsNull() {
        log.debug("Request to get all employmentContracts where Employee is null");
        return StreamSupport.stream(employmentContractRepository.findAll().spliterator(), false)
            .filter(employmentContract -> employmentContract.getEmployee() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmploymentContract> findOne(Long id) {
        log.debug("Request to get EmploymentContract : {}", id);
        return employmentContractRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EmploymentContract : {}", id);
        employmentContractRepository.deleteById(id);
    }
}
