package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.IdDocument;
import io.simpleit.umbrella.repository.IdDocumentRepository;
import io.simpleit.umbrella.service.IdDocumentService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.IdDocument}.
 */
@Service
@Transactional
public class IdDocumentServiceImpl implements IdDocumentService {

    private final Logger log = LoggerFactory.getLogger(IdDocumentServiceImpl.class);

    private final IdDocumentRepository idDocumentRepository;

    public IdDocumentServiceImpl(IdDocumentRepository idDocumentRepository) {
        this.idDocumentRepository = idDocumentRepository;
    }

    @Override
    public IdDocument save(IdDocument idDocument) {
        log.debug("Request to save IdDocument : {}", idDocument);
        return idDocumentRepository.save(idDocument);
    }

    @Override
    public IdDocument update(IdDocument idDocument) {
        log.debug("Request to update IdDocument : {}", idDocument);
        return idDocumentRepository.save(idDocument);
    }

    @Override
    public Optional<IdDocument> partialUpdate(IdDocument idDocument) {
        log.debug("Request to partially update IdDocument : {}", idDocument);

        return idDocumentRepository
            .findById(idDocument.getId())
            .map(existingIdDocument -> {
                if (idDocument.getIdType() != null) {
                    existingIdDocument.setIdType(idDocument.getIdType());
                }
                if (idDocument.getIdNumber() != null) {
                    existingIdDocument.setIdNumber(idDocument.getIdNumber());
                }

                return existingIdDocument;
            })
            .map(idDocumentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IdDocument> findAll(Pageable pageable) {
        log.debug("Request to get all IdDocuments");
        return idDocumentRepository.findAll(pageable);
    }

    /**
     *  Get all the idDocuments where Employee is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IdDocument> findAllWhereEmployeeIsNull() {
        log.debug("Request to get all idDocuments where Employee is null");
        return StreamSupport.stream(idDocumentRepository.findAll().spliterator(), false)
            .filter(idDocument -> idDocument.getEmployee() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdDocument> findOne(Long id) {
        log.debug("Request to get IdDocument : {}", id);
        return idDocumentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IdDocument : {}", id);
        idDocumentRepository.deleteById(id);
    }
}
