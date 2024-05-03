package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.EmploymentContract;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmploymentContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmploymentContractRepository extends JpaRepository<EmploymentContract, Long> {}
