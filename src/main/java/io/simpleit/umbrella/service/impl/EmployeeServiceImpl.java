package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Employee;
import io.simpleit.umbrella.repository.EmployeeRepository;
import io.simpleit.umbrella.service.EmployeeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        log.debug("Request to update Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> partialUpdate(Employee employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(existingEmployee -> {
                if (employee.getEmployeeNumber() != null) {
                    existingEmployee.setEmployeeNumber(employee.getEmployeeNumber());
                }
                if (employee.getBirthDate() != null) {
                    existingEmployee.setBirthDate(employee.getBirthDate());
                }
                if (employee.getBirthPlace() != null) {
                    existingEmployee.setBirthPlace(employee.getBirthPlace());
                }
                if (employee.getNationality() != null) {
                    existingEmployee.setNationality(employee.getNationality());
                }
                if (employee.getGender() != null) {
                    existingEmployee.setGender(employee.getGender());
                }
                if (employee.getMaritalStatus() != null) {
                    existingEmployee.setMaritalStatus(employee.getMaritalStatus());
                }
                if (employee.getDependentChildrenNumber() != null) {
                    existingEmployee.setDependentChildrenNumber(employee.getDependentChildrenNumber());
                }
                if (employee.getSocialSecurityNumber() != null) {
                    existingEmployee.setSocialSecurityNumber(employee.getSocialSecurityNumber());
                }
                if (employee.getIban() != null) {
                    existingEmployee.setIban(employee.getIban());
                }
                if (employee.getBicSwift() != null) {
                    existingEmployee.setBicSwift(employee.getBicSwift());
                }

                return existingEmployee;
            })
            .map(employeeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
