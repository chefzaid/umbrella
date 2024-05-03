package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Enterprise;
import io.simpleit.umbrella.repository.EnterpriseRepository;
import io.simpleit.umbrella.service.EnterpriseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Enterprise}.
 */
@Service
@Transactional
public class EnterpriseServiceImpl implements EnterpriseService {

    private final Logger log = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseServiceImpl(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    @Override
    public Enterprise save(Enterprise enterprise) {
        log.debug("Request to save Enterprise : {}", enterprise);
        return enterpriseRepository.save(enterprise);
    }

    @Override
    public Enterprise update(Enterprise enterprise) {
        log.debug("Request to update Enterprise : {}", enterprise);
        return enterpriseRepository.save(enterprise);
    }

    @Override
    public Optional<Enterprise> partialUpdate(Enterprise enterprise) {
        log.debug("Request to partially update Enterprise : {}", enterprise);

        return enterpriseRepository
            .findById(enterprise.getId())
            .map(existingEnterprise -> {
                if (enterprise.getName() != null) {
                    existingEnterprise.setName(enterprise.getName());
                }
                if (enterprise.getCompanyStatus() != null) {
                    existingEnterprise.setCompanyStatus(enterprise.getCompanyStatus());
                }
                if (enterprise.getLogo() != null) {
                    existingEnterprise.setLogo(enterprise.getLogo());
                }
                if (enterprise.getLogoContentType() != null) {
                    existingEnterprise.setLogoContentType(enterprise.getLogoContentType());
                }
                if (enterprise.getSiret() != null) {
                    existingEnterprise.setSiret(enterprise.getSiret());
                }
                if (enterprise.getSiren() != null) {
                    existingEnterprise.setSiren(enterprise.getSiren());
                }
                if (enterprise.getSalesTaxNumber() != null) {
                    existingEnterprise.setSalesTaxNumber(enterprise.getSalesTaxNumber());
                }
                if (enterprise.getIban() != null) {
                    existingEnterprise.setIban(enterprise.getIban());
                }
                if (enterprise.getBicSwift() != null) {
                    existingEnterprise.setBicSwift(enterprise.getBicSwift());
                }
                if (enterprise.getWebsite() != null) {
                    existingEnterprise.setWebsite(enterprise.getWebsite());
                }
                if (enterprise.getDefaultInvoiceTerms() != null) {
                    existingEnterprise.setDefaultInvoiceTerms(enterprise.getDefaultInvoiceTerms());
                }

                return existingEnterprise;
            })
            .map(enterpriseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Enterprise> findAll(Pageable pageable) {
        log.debug("Request to get all Enterprises");
        return enterpriseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Enterprise> findOne(Long id) {
        log.debug("Request to get Enterprise : {}", id);
        return enterpriseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Enterprise : {}", id);
        enterpriseRepository.deleteById(id);
    }
}
