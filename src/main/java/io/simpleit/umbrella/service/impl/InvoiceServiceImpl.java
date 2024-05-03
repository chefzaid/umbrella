package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Invoice;
import io.simpleit.umbrella.repository.InvoiceRepository;
import io.simpleit.umbrella.service.InvoiceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Invoice}.
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice update(Invoice invoice) {
        log.debug("Request to update Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Optional<Invoice> partialUpdate(Invoice invoice) {
        log.debug("Request to partially update Invoice : {}", invoice);

        return invoiceRepository
            .findById(invoice.getId())
            .map(existingInvoice -> {
                if (invoice.getInvoiceNumber() != null) {
                    existingInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
                }
                if (invoice.getLabel() != null) {
                    existingInvoice.setLabel(invoice.getLabel());
                }
                if (invoice.getIssueDate() != null) {
                    existingInvoice.setIssueDate(invoice.getIssueDate());
                }
                if (invoice.getCurrency() != null) {
                    existingInvoice.setCurrency(invoice.getCurrency());
                }
                if (invoice.getSalesTaxPct() != null) {
                    existingInvoice.setSalesTaxPct(invoice.getSalesTaxPct());
                }
                if (invoice.getTerms() != null) {
                    existingInvoice.setTerms(invoice.getTerms());
                }

                return existingInvoice;
            })
            .map(invoiceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }
}
