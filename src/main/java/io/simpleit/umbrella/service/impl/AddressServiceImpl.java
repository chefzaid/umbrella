package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Address;
import io.simpleit.umbrella.repository.AddressRepository;
import io.simpleit.umbrella.service.AddressService;
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
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Address}.
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(Address address) {
        log.debug("Request to save Address : {}", address);
        return addressRepository.save(address);
    }

    @Override
    public Address update(Address address) {
        log.debug("Request to update Address : {}", address);
        return addressRepository.save(address);
    }

    @Override
    public Optional<Address> partialUpdate(Address address) {
        log.debug("Request to partially update Address : {}", address);

        return addressRepository
            .findById(address.getId())
            .map(existingAddress -> {
                if (address.getStreetAddress() != null) {
                    existingAddress.setStreetAddress(address.getStreetAddress());
                }
                if (address.getPostalCode() != null) {
                    existingAddress.setPostalCode(address.getPostalCode());
                }
                if (address.getCity() != null) {
                    existingAddress.setCity(address.getCity());
                }
                if (address.getCountry() != null) {
                    existingAddress.setCountry(address.getCountry());
                }

                return existingAddress;
            })
            .map(addressRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Address> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll(pageable);
    }

    /**
     *  Get all the addresses where Contact is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Address> findAllWhereContactIsNull() {
        log.debug("Request to get all addresses where Contact is null");
        return StreamSupport.stream(addressRepository.findAll().spliterator(), false)
            .filter(address -> address.getContact() == null)
            .toList();
    }

    /**
     *  Get all the addresses where Employee is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Address> findAllWhereEmployeeIsNull() {
        log.debug("Request to get all addresses where Employee is null");
        return StreamSupport.stream(addressRepository.findAll().spliterator(), false)
            .filter(address -> address.getEmployee() == null)
            .toList();
    }

    /**
     *  Get all the addresses where Client is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Address> findAllWhereClientIsNull() {
        log.debug("Request to get all addresses where Client is null");
        return StreamSupport.stream(addressRepository.findAll().spliterator(), false)
            .filter(address -> address.getClient() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
    }
}
