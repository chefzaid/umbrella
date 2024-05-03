package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.Enterprise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Enterprise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {}
