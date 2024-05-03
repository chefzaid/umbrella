package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Document;
import io.simpleit.umbrella.repository.DocumentRepository;
import io.simpleit.umbrella.service.DocumentService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Document}.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document save(Document document) {
        log.debug("Request to save Document : {}", document);
        return documentRepository.save(document);
    }

    @Override
    public Document update(Document document) {
        log.debug("Request to update Document : {}", document);
        return documentRepository.save(document);
    }

    @Override
    public Optional<Document> partialUpdate(Document document) {
        log.debug("Request to partially update Document : {}", document);

        return documentRepository
            .findById(document.getId())
            .map(existingDocument -> {
                if (document.getLabel() != null) {
                    existingDocument.setLabel(document.getLabel());
                }
                if (document.getUploadDate() != null) {
                    existingDocument.setUploadDate(document.getUploadDate());
                }
                if (document.getIssuingDate() != null) {
                    existingDocument.setIssuingDate(document.getIssuingDate());
                }
                if (document.getExpirationDate() != null) {
                    existingDocument.setExpirationDate(document.getExpirationDate());
                }
                if (document.getFile() != null) {
                    existingDocument.setFile(document.getFile());
                }
                if (document.getFileContentType() != null) {
                    existingDocument.setFileContentType(document.getFileContentType());
                }

                return existingDocument;
            })
            .map(documentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        return documentRepository.findAll(pageable);
    }

    /**
     *  Get all the documents where IdDocument is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Document> findAllWhereIdDocumentIsNull() {
        log.debug("Request to get all documents where IdDocument is null");
        return StreamSupport.stream(documentRepository.findAll().spliterator(), false)
            .filter(document -> document.getIdDocument() == null)
            .toList();
    }

    /**
     *  Get all the documents where TimeSheet is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Document> findAllWhereTimeSheetIsNull() {
        log.debug("Request to get all documents where TimeSheet is null");
        return StreamSupport.stream(documentRepository.findAll().spliterator(), false)
            .filter(document -> document.getTimeSheet() == null)
            .toList();
    }

    /**
     *  Get all the documents where ExpenseNote is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Document> findAllWhereExpenseNoteIsNull() {
        log.debug("Request to get all documents where ExpenseNote is null");
        return StreamSupport.stream(documentRepository.findAll().spliterator(), false)
            .filter(document -> document.getExpenseNote() == null)
            .toList();
    }

    /**
     *  Get all the documents where Invoice is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Document> findAllWhereInvoiceIsNull() {
        log.debug("Request to get all documents where Invoice is null");
        return StreamSupport.stream(documentRepository.findAll().spliterator(), false)
            .filter(document -> document.getInvoice() == null)
            .toList();
    }

    /**
     *  Get all the documents where PaySlip is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Document> findAllWherePaySlipIsNull() {
        log.debug("Request to get all documents where PaySlip is null");
        return StreamSupport.stream(documentRepository.findAll().spliterator(), false)
            .filter(document -> document.getPaySlip() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Document> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
    }
}
