package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Prospect;
import io.simpleit.umbrella.repository.ProspectRepository;
import io.simpleit.umbrella.service.ProspectService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Prospect}.
 */
@Service
@Transactional
public class ProspectServiceImpl implements ProspectService {

    private final Logger log = LoggerFactory.getLogger(ProspectServiceImpl.class);

    private final ProspectRepository prospectRepository;

    public ProspectServiceImpl(ProspectRepository prospectRepository) {
        this.prospectRepository = prospectRepository;
    }

    @Override
    public Prospect save(Prospect prospect) {
        log.debug("Request to save Prospect : {}", prospect);
        return prospectRepository.save(prospect);
    }

    @Override
    public Prospect update(Prospect prospect) {
        log.debug("Request to update Prospect : {}", prospect);
        return prospectRepository.save(prospect);
    }

    @Override
    public Optional<Prospect> partialUpdate(Prospect prospect) {
        log.debug("Request to partially update Prospect : {}", prospect);

        return prospectRepository
            .findById(prospect.getId())
            .map(existingProspect -> {
                if (prospect.getDailyRate() != null) {
                    existingProspect.setDailyRate(prospect.getDailyRate());
                }
                if (prospect.getMonthlyExpenses() != null) {
                    existingProspect.setMonthlyExpenses(prospect.getMonthlyExpenses());
                }
                if (prospect.getTaxRate() != null) {
                    existingProspect.setTaxRate(prospect.getTaxRate());
                }
                if (prospect.getExpectedStartDate() != null) {
                    existingProspect.setExpectedStartDate(prospect.getExpectedStartDate());
                }
                if (prospect.getExpectedClient() != null) {
                    existingProspect.setExpectedClient(prospect.getExpectedClient());
                }
                if (prospect.getNotes() != null) {
                    existingProspect.setNotes(prospect.getNotes());
                }

                return existingProspect;
            })
            .map(prospectRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Prospect> findAll(Pageable pageable) {
        log.debug("Request to get all Prospects");
        return prospectRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prospect> findOne(Long id) {
        log.debug("Request to get Prospect : {}", id);
        return prospectRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prospect : {}", id);
        prospectRepository.deleteById(id);
    }
}
