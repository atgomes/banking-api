package com.github.atgomes.service.bankingapi.web.rest;
import com.github.atgomes.service.bankingapi.service.BalanceService;
import com.github.atgomes.service.bankingapi.web.rest.errors.BadRequestAlertException;
import com.github.atgomes.service.bankingapi.web.rest.util.HeaderUtil;
import com.github.atgomes.service.bankingapi.service.dto.BalanceDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Balance.
 */
@RestController
@RequestMapping("/api")
public class BalanceResource {

    private final Logger log = LoggerFactory.getLogger(BalanceResource.class);

    private static final String ENTITY_NAME = "bankingApiBalance";

    private final BalanceService balanceService;

    public BalanceResource(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    /**
     * POST  /balances : Create a new balance.
     *
     * @param balanceDTO the balanceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new balanceDTO, or with status 400 (Bad Request) if the balance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/balances")
    public ResponseEntity<BalanceDTO> createBalance(@RequestBody BalanceDTO balanceDTO) throws URISyntaxException {
        log.debug("REST request to save Balance : {}", balanceDTO);
        if (balanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new balance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BalanceDTO result = balanceService.save(balanceDTO);
        return ResponseEntity.created(new URI("/api/balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /balances : Updates an existing balance.
     *
     * @param balanceDTO the balanceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated balanceDTO,
     * or with status 400 (Bad Request) if the balanceDTO is not valid,
     * or with status 500 (Internal Server Error) if the balanceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/balances")
    public ResponseEntity<BalanceDTO> updateBalance(@RequestBody BalanceDTO balanceDTO) throws URISyntaxException {
        log.debug("REST request to update Balance : {}", balanceDTO);
        if (balanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BalanceDTO result = balanceService.save(balanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, balanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /balances : get all the balances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of balances in body
     */
    @GetMapping("/balances")
    public List<BalanceDTO> getAllBalances() {
        log.debug("REST request to get all Balances");
        return balanceService.findAll();
    }

    /**
     * GET  /balances/:id : get the "id" balance.
     *
     * @param id the id of the balanceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the balanceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/balances/{id}")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable Long id) {
        log.debug("REST request to get Balance : {}", id);
        Optional<BalanceDTO> balanceDTO = balanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(balanceDTO);
    }

    /**
     * DELETE  /balances/:id : delete the "id" balance.
     *
     * @param id the id of the balanceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/balances/{id}")
    public ResponseEntity<Void> deleteBalance(@PathVariable Long id) {
        log.debug("REST request to delete Balance : {}", id);
        balanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
