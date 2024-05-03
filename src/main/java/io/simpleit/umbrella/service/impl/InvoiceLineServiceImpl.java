package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.InvoiceLine;
import io.simpleit.umbrella.repository.InvoiceLineRepository;
import io.simpleit.umbrella.service.InvoiceLineService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.InvoiceLine}.
 */
@Service
@Transactional
public class InvoiceLineServiceImpl implements InvoiceLineService {

    private final Logger log = LoggerFactory.getLogger(InvoiceLineServiceImpl.class);

    private final InvoiceLineRepository invoiceLineRepository;

    public InvoiceLineServiceImpl(InvoiceLineRepository invoiceLineRepository) {
        this.invoiceLineRepository = invoiceLineRepository;
    }

    @Override
    public InvoiceLine save(InvoiceLine invoiceLine) {
        log.debug("Request to save InvoiceLine : {}", invoiceLine);
        return invoiceLineRepository.save(invoiceLine);
    }

    @Override
    public InvoiceLine update(InvoiceLine invoiceLine) {
        log.debug("Request to update InvoiceLine : {}", invoiceLine);
        return invoiceLineRepository.save(invoiceLine);
    }

    @Override
    public Optional<InvoiceLine> partialUpdate(InvoiceLine invoiceLine) {
        log.debug("Request to partially update InvoiceLine : {}", invoiceLine);

        return invoiceLineRepository
            .findById(invoiceLine.getId())
            .map(existingInvoiceLine -> {
                if (invoiceLine.getLabel() != null) {
                    existingInvoiceLine.setLabel(invoiceLine.getLabel());
                }
                if (invoiceLine.getDescription() != null) {
                    existingInvoiceLine.setDescription(invoiceLine.getDescription());
                }
                if (invoiceLine.getPrice() != null) {
                    existingInvoiceLine.setPrice(invoiceLine.getPrice());
                }
                if (invoiceLine.getQuantity() != null) {
                    existingInvoiceLine.setQuantity(invoiceLine.getQuantity());
                }

                return existingInvoiceLine;
            })
            .map(invoiceLineRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceLine> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceLines");
        return invoiceLineRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceLine> findOne(Long id) {
        log.debug("Request to get InvoiceLine : {}", id);
        return invoiceLineRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InvoiceLine : {}", id);
        invoiceLineRepository.deleteById(id);
    }
}
