package io.simpleit.umbrella.repository;

import io.simpleit.umbrella.domain.PaySlip;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaySlip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {}
