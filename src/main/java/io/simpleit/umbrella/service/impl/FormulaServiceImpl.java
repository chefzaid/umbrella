package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Formula;
import io.simpleit.umbrella.repository.FormulaRepository;
import io.simpleit.umbrella.service.FormulaService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Formula}.
 */
@Service
@Transactional
public class FormulaServiceImpl implements FormulaService {

    private final Logger log = LoggerFactory.getLogger(FormulaServiceImpl.class);

    private final FormulaRepository formulaRepository;

    public FormulaServiceImpl(FormulaRepository formulaRepository) {
        this.formulaRepository = formulaRepository;
    }

    @Override
    public Formula save(Formula formula) {
        log.debug("Request to save Formula : {}", formula);
        return formulaRepository.save(formula);
    }

    @Override
    public Formula update(Formula formula) {
        log.debug("Request to update Formula : {}", formula);
        return formulaRepository.save(formula);
    }

    @Override
    public Optional<Formula> partialUpdate(Formula formula) {
        log.debug("Request to partially update Formula : {}", formula);

        return formulaRepository
            .findById(formula.getId())
            .map(existingFormula -> {
                if (formula.getLabel() != null) {
                    existingFormula.setLabel(formula.getLabel());
                }
                if (formula.getAdminFeesPct() != null) {
                    existingFormula.setAdminFeesPct(formula.getAdminFeesPct());
                }
                if (formula.getAdditionalFeesPct() != null) {
                    existingFormula.setAdditionalFeesPct(formula.getAdditionalFeesPct());
                }

                return existingFormula;
            })
            .map(formulaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Formula> findAll(Pageable pageable) {
        log.debug("Request to get all Formulas");
        return formulaRepository.findAll(pageable);
    }

    /**
     *  Get all the formulas where Prospect is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Formula> findAllWhereProspectIsNull() {
        log.debug("Request to get all formulas where Prospect is null");
        return StreamSupport.stream(formulaRepository.findAll().spliterator(), false)
            .filter(formula -> formula.getProspect() == null)
            .toList();
    }

    /**
     *  Get all the formulas where EmploymentContract is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Formula> findAllWhereEmploymentContractIsNull() {
        log.debug("Request to get all formulas where EmploymentContract is null");
        return StreamSupport.stream(formulaRepository.findAll().spliterator(), false)
            .filter(formula -> formula.getEmploymentContract() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Formula> findOne(Long id) {
        log.debug("Request to get Formula : {}", id);
        return formulaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Formula : {}", id);
        formulaRepository.deleteById(id);
    }
}
