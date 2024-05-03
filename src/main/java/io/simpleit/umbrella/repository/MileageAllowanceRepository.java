package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.MileageAllowance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MileageAllowance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MileageAllowanceRepository extends JpaRepository<MileageAllowance, Long> {}
