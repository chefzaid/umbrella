package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.AppUser;
import io.simpleit.umbrella.repository.AppUserRepository;
import io.simpleit.umbrella.service.AppUserService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.AppUser}.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser save(AppUser appUser) {
        log.debug("Request to save AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser update(AppUser appUser) {
        log.debug("Request to update AppUser : {}", appUser);
        return appUserRepository.save(appUser);
    }

    @Override
    public Optional<AppUser> partialUpdate(AppUser appUser) {
        log.debug("Request to partially update AppUser : {}", appUser);

        return appUserRepository
            .findById(appUser.getId())
            .map(existingAppUser -> {
                if (appUser.getUsername() != null) {
                    existingAppUser.setUsername(appUser.getUsername());
                }
                if (appUser.getPassword() != null) {
                    existingAppUser.setPassword(appUser.getPassword());
                }
                if (appUser.getPhoto() != null) {
                    existingAppUser.setPhoto(appUser.getPhoto());
                }
                if (appUser.getPhotoContentType() != null) {
                    existingAppUser.setPhotoContentType(appUser.getPhotoContentType());
                }
                if (appUser.getIsAdmin() != null) {
                    existingAppUser.setIsAdmin(appUser.getIsAdmin());
                }
                if (appUser.getCreationDate() != null) {
                    existingAppUser.setCreationDate(appUser.getCreationDate());
                }

                return existingAppUser;
            })
            .map(appUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppUser> findAll(Pageable pageable) {
        log.debug("Request to get all AppUsers");
        return appUserRepository.findAll(pageable);
    }

    /**
     *  Get all the appUsers where Employee is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppUser> findAllWhereEmployeeIsNull() {
        log.debug("Request to get all appUsers where Employee is null");
        return StreamSupport.stream(appUserRepository.findAll().spliterator(), false)
            .filter(appUser -> appUser.getEmployee() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppUser> findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        return appUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
    }
}
