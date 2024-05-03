package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Wallet;
import io.simpleit.umbrella.repository.WalletRepository;
import io.simpleit.umbrella.service.WalletService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Wallet}.
 */
@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public Wallet save(Wallet wallet) {
        log.debug("Request to save Wallet : {}", wallet);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet update(Wallet wallet) {
        log.debug("Request to update Wallet : {}", wallet);
        return walletRepository.save(wallet);
    }

    @Override
    public Optional<Wallet> partialUpdate(Wallet wallet) {
        log.debug("Request to partially update Wallet : {}", wallet);

        return walletRepository
            .findById(wallet.getId())
            .map(existingWallet -> {
                if (wallet.getTotalBalance() != null) {
                    existingWallet.setTotalBalance(wallet.getTotalBalance());
                }
                if (wallet.getTotalProvision() != null) {
                    existingWallet.setTotalProvision(wallet.getTotalProvision());
                }

                return existingWallet;
            })
            .map(walletRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Wallet> findAll(Pageable pageable) {
        log.debug("Request to get all Wallets");
        return walletRepository.findAll(pageable);
    }

    /**
     *  Get all the wallets where Employee is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Wallet> findAllWhereEmployeeIsNull() {
        log.debug("Request to get all wallets where Employee is null");
        return StreamSupport.stream(walletRepository.findAll().spliterator(), false)
            .filter(wallet -> wallet.getEmployee() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Wallet> findOne(Long id) {
        log.debug("Request to get Wallet : {}", id);
        return walletRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Wallet : {}", id);
        walletRepository.deleteById(id);
    }
}
