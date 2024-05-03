package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.PaySlip;
import io.simpleit.umbrella.repository.PaySlipRepository;
import io.simpleit.umbrella.service.PaySlipService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.PaySlip}.
 */
@Service
@Transactional
public class PaySlipServiceImpl implements PaySlipService {

    private final Logger log = LoggerFactory.getLogger(PaySlipServiceImpl.class);

    private final PaySlipRepository paySlipRepository;

    public PaySlipServiceImpl(PaySlipRepository paySlipRepository) {
        this.paySlipRepository = paySlipRepository;
    }

    @Override
    public PaySlip save(PaySlip paySlip) {
        log.debug("Request to save PaySlip : {}", paySlip);
        return paySlipRepository.save(paySlip);
    }

    @Override
    public PaySlip update(PaySlip paySlip) {
        log.debug("Request to update PaySlip : {}", paySlip);
        return paySlipRepository.save(paySlip);
    }

    @Override
    public Optional<PaySlip> partialUpdate(PaySlip paySlip) {
        log.debug("Request to partially update PaySlip : {}", paySlip);

        return paySlipRepository
            .findById(paySlip.getId())
            .map(existingPaySlip -> {
                if (paySlip.getSuperGrossSalary() != null) {
                    existingPaySlip.setSuperGrossSalary(paySlip.getSuperGrossSalary());
                }
                if (paySlip.getGrossSalary() != null) {
                    existingPaySlip.setGrossSalary(paySlip.getGrossSalary());
                }
                if (paySlip.getNetSalary() != null) {
                    existingPaySlip.setNetSalary(paySlip.getNetSalary());
                }
                if (paySlip.getTaxRate() != null) {
                    existingPaySlip.setTaxRate(paySlip.getTaxRate());
                }
                if (paySlip.getAmountPaid() != null) {
                    existingPaySlip.setAmountPaid(paySlip.getAmountPaid());
                }
                if (paySlip.getTotalExpenses() != null) {
                    existingPaySlip.setTotalExpenses(paySlip.getTotalExpenses());
                }

                return existingPaySlip;
            })
            .map(paySlipRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaySlip> findAll(Pageable pageable) {
        log.debug("Request to get all PaySlips");
        return paySlipRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaySlip> findOne(Long id) {
        log.debug("Request to get PaySlip : {}", id);
        return paySlipRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaySlip : {}", id);
        paySlipRepository.deleteById(id);
    }
}
