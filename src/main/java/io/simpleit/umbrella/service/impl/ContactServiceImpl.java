package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Contact;
import io.simpleit.umbrella.repository.ContactRepository;
import io.simpleit.umbrella.service.ContactService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Contact}.
 */
@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact save(Contact contact) {
        log.debug("Request to save Contact : {}", contact);
        return contactRepository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        log.debug("Request to update Contact : {}", contact);
        return contactRepository.save(contact);
    }

    @Override
    public Optional<Contact> partialUpdate(Contact contact) {
        log.debug("Request to partially update Contact : {}", contact);

        return contactRepository
            .findById(contact.getId())
            .map(existingContact -> {
                if (contact.getFirstName() != null) {
                    existingContact.setFirstName(contact.getFirstName());
                }
                if (contact.getLastName() != null) {
                    existingContact.setLastName(contact.getLastName());
                }
                if (contact.getEmail() != null) {
                    existingContact.setEmail(contact.getEmail());
                }
                if (contact.getPhoneNumber() != null) {
                    existingContact.setPhoneNumber(contact.getPhoneNumber());
                }
                if (contact.getJobTitle() != null) {
                    existingContact.setJobTitle(contact.getJobTitle());
                }

                return existingContact;
            })
            .map(contactRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contact> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        return contactRepository.findAll(pageable);
    }

    /**
     *  Get all the contacts where AppUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Contact> findAllWhereAppUserIsNull() {
        log.debug("Request to get all contacts where AppUser is null");
        return StreamSupport.stream(contactRepository.findAll().spliterator(), false)
            .filter(contact -> contact.getAppUser() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contact> findOne(Long id) {
        log.debug("Request to get Contact : {}", id);
        return contactRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contact : {}", id);
        contactRepository.deleteById(id);
    }
}
