package io.simpleit.umbrella.web.rest;

import io.simpleit.umbrella.domain.Wallet;
import io.simpleit.umbrella.repository.WalletRepository;
import io.simpleit.umbrella.service.WalletService;
import io.simpleit.umbrella.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.simpleit.umbrella.domain.Wallet}.
 */
@RestController
@RequestMapping("/api/wallets")
public class WalletResource {

    private final Logger log = LoggerFactory.getLogger(WalletResource.class);

    private static final String ENTITY_NAME = "wallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalletService walletService;

    private final WalletRepository walletRepository;

    public WalletResource(WalletService walletService, WalletRepository walletRepository) {
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    /**
     * {@code POST  /wallets} : Create a new wallet.
     *
     * @param wallet the wallet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wallet, or with status {@code 400 (Bad Request)} if the wallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody Wallet wallet) throws URISyntaxException {
        log.debug("REST request to save Wallet : {}", wallet);
        if (wallet.getId() != null) {
            throw new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        wallet = walletService.save(wallet);
        return ResponseEntity.created(new URI("/api/wallets/" + wallet.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, wallet.getId().toString()))
            .body(wallet);
    }

    /**
     * {@code PUT  /wallets/:id} : Updates an existing wallet.
     *
     * @param id the id of the wallet to save.
     * @param wallet the wallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wallet,
     * or with status {@code 400 (Bad Request)} if the wallet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Wallet> updateWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Wallet wallet
    ) throws URISyntaxException {
        log.debug("REST request to update Wallet : {}, {}", id, wallet);
        if (wallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        wallet = walletService.update(wallet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wallet.getId().toString()))
            .body(wallet);
    }

    /**
     * {@code PATCH  /wallets/:id} : Partial updates given fields of an existing wallet, field will ignore if it is null
     *
     * @param id the id of the wallet to save.
     * @param wallet the wallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wallet,
     * or with status {@code 400 (Bad Request)} if the wallet is not valid,
     * or with status {@code 404 (Not Found)} if the wallet is not found,
     * or with status {@code 500 (Internal Server Error)} if the wallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Wallet> partialUpdateWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Wallet wallet
    ) throws URISyntaxException {
        log.debug("REST request to partial update Wallet partially : {}, {}", id, wallet);
        if (wallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Wallet> result = walletService.partialUpdate(wallet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wallet.getId().toString())
        );
    }

    /**
     * {@code GET  /wallets} : get all the wallets.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wallets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Wallet>> getAllWallets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("employee-is-null".equals(filter)) {
            log.debug("REST request to get all Wallets where employee is null");
            return new ResponseEntity<>(walletService.findAllWhereEmployeeIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Wallets");
        Page<Wallet> page = walletService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wallets/:id} : get the "id" wallet.
     *
     * @param id the id of the wallet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wallet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable("id") Long id) {
        log.debug("REST request to get Wallet : {}", id);
        Optional<Wallet> wallet = walletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wallet);
    }

    /**
     * {@code DELETE  /wallets/:id} : delete the "id" wallet.
     *
     * @param id the id of the wallet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable("id") Long id) {
        log.debug("REST request to delete Wallet : {}", id);
        walletService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
