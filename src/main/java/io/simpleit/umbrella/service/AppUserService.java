package io.simpleit.umbrella.service;

import io.simpleit.umbrella.domain.AppUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.simpleit.umbrella.domain.AppUser}.
 */
public interface AppUserService {
    /**
     * Save a appUser.
     *
     * @param appUser the entity to save.
     * @return the persisted entity.
     */
    AppUser save(AppUser appUser);

    /**
     * Updates a appUser.
     *
     * @param appUser the entity to update.
     * @return the persisted entity.
     */
    AppUser update(AppUser appUser);

    /**
     * Partially updates a appUser.
     *
     * @param appUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppUser> partialUpdate(AppUser appUser);

    /**
     * Get all the appUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppUser> findAll(Pageable pageable);

    /**
     * Get all the AppUser where Employee is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AppUser> findAllWhereEmployeeIsNull();

    /**
     * Get the "id" appUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUser> findOne(Long id);

    /**
     * Delete the "id" appUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
