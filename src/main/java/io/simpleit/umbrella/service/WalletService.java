package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.Wallet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.Wallet}.
 */
public interface WalletService {
    /**
     * Save a wallet.
     *
     * @param wallet the entity to save.
     * @return the persisted entity.
     */
    Wallet save(Wallet wallet);

    /**
     * Updates a wallet.
     *
     * @param wallet the entity to update.
     * @return the persisted entity.
     */
    Wallet update(Wallet wallet);

    /**
     * Partially updates a wallet.
     *
     * @param wallet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Wallet> partialUpdate(Wallet wallet);

    /**
     * Get all the wallets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Wallet> findAll(Pageable pageable);

    /**
     * Get all the Wallet where Employee is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Wallet> findAllWhereEmployeeIsNull();

    /**
     * Get the "id" wallet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Wallet> findOne(Long id);

    /**
     * Delete the "id" wallet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
