package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Notification;
import io.simpleit.umbrella.repository.NotificationRepository;
import io.simpleit.umbrella.service.NotificationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Notification}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Notification notification) {
        log.debug("Request to update Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> partialUpdate(Notification notification) {
        log.debug("Request to partially update Notification : {}", notification);

        return notificationRepository
            .findById(notification.getId())
            .map(existingNotification -> {
                if (notification.getTitle() != null) {
                    existingNotification.setTitle(notification.getTitle());
                }
                if (notification.getDescription() != null) {
                    existingNotification.setDescription(notification.getDescription());
                }
                if (notification.getCreationDate() != null) {
                    existingNotification.setCreationDate(notification.getCreationDate());
                }
                if (notification.getReadDate() != null) {
                    existingNotification.setReadDate(notification.getReadDate());
                }
                if (notification.getHide() != null) {
                    existingNotification.setHide(notification.getHide());
                }

                return existingNotification;
            })
            .map(notificationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }
}
