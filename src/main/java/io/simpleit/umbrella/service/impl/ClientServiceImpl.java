package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Client;
import io.simpleit.umbrella.repository.ClientRepository;
import io.simpleit.umbrella.service.ClientService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Client}.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        log.debug("Request to save Client : {}", client);
        return clientRepository.save(client);
    }

    @Override
    public Client update(Client client) {
        log.debug("Request to update Client : {}", client);
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> partialUpdate(Client client) {
        log.debug("Request to partially update Client : {}", client);

        return clientRepository
            .findById(client.getId())
            .map(existingClient -> {
                if (client.getName() != null) {
                    existingClient.setName(client.getName());
                }
                if (client.getCompanyStatus() != null) {
                    existingClient.setCompanyStatus(client.getCompanyStatus());
                }

                return existingClient;
            })
            .map(clientRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable);
    }

    /**
     *  Get all the clients where ServiceContract is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Client> findAllWhereServiceContractIsNull() {
        log.debug("Request to get all clients where ServiceContract is null");
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false)
            .filter(client -> client.getServiceContract() == null)
            .toList();
    }

    /**
     *  Get all the clients where Project is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Client> findAllWhereProjectIsNull() {
        log.debug("Request to get all clients where Project is null");
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false).filter(client -> client.getProject() == null).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }
}
