package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.ParameterGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ParameterGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterGroupRepository extends JpaRepository<ParameterGroup, Long> {}
