package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.MileageAllowance;
import io.simpleit.umbrella.repository.MileageAllowanceRepository;
import io.simpleit.umbrella.service.MileageAllowanceService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.MileageAllowance}.
 */
@Service
@Transactional
public class MileageAllowanceServiceImpl implements MileageAllowanceService {

    private final Logger log = LoggerFactory.getLogger(MileageAllowanceServiceImpl.class);

    private final MileageAllowanceRepository mileageAllowanceRepository;

    public MileageAllowanceServiceImpl(MileageAllowanceRepository mileageAllowanceRepository) {
        this.mileageAllowanceRepository = mileageAllowanceRepository;
    }

    @Override
    public MileageAllowance save(MileageAllowance mileageAllowance) {
        log.debug("Request to save MileageAllowance : {}", mileageAllowance);
        return mileageAllowanceRepository.save(mileageAllowance);
    }

    @Override
    public MileageAllowance update(MileageAllowance mileageAllowance) {
        log.debug("Request to update MileageAllowance : {}", mileageAllowance);
        return mileageAllowanceRepository.save(mileageAllowance);
    }

    @Override
    public Optional<MileageAllowance> partialUpdate(MileageAllowance mileageAllowance) {
        log.debug("Request to partially update MileageAllowance : {}", mileageAllowance);

        return mileageAllowanceRepository
            .findById(mileageAllowance.getId())
            .map(existingMileageAllowance -> {
                if (mileageAllowance.getMileage() != null) {
                    existingMileageAllowance.setMileage(mileageAllowance.getMileage());
                }
                if (mileageAllowance.getMultiplier() != null) {
                    existingMileageAllowance.setMultiplier(mileageAllowance.getMultiplier());
                }

                return existingMileageAllowance;
            })
            .map(mileageAllowanceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MileageAllowance> findAll(Pageable pageable) {
        log.debug("Request to get all MileageAllowances");
        return mileageAllowanceRepository.findAll(pageable);
    }

    /**
     *  Get all the mileageAllowances where ExpenseNote is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MileageAllowance> findAllWhereExpenseNoteIsNull() {
        log.debug("Request to get all mileageAllowances where ExpenseNote is null");
        return StreamSupport.stream(mileageAllowanceRepository.findAll().spliterator(), false)
            .filter(mileageAllowance -> mileageAllowance.getExpenseNote() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MileageAllowance> findOne(Long id) {
        log.debug("Request to get MileageAllowance : {}", id);
        return mileageAllowanceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MileageAllowance : {}", id);
        mileageAllowanceRepository.deleteById(id);
    }
}
