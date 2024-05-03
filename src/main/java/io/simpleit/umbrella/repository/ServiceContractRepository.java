package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.ServiceContract;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceContractRepository extends JpaRepository<ServiceContract, Long> {}
