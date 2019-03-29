package com.github.atgomes.service.bankingapi.service;

import com.github.atgomes.service.bankingapi.service.dto.BalanceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Balance.
 */
public interface BalanceService {

    /**
     * Save a balance.
     *
     * @param balanceDTO the entity to save
     * @return the persisted entity
     */
    BalanceDTO save(BalanceDTO balanceDTO);

    /**
     * Get all the balances.
     *
     * @return the list of entities
     */
    List<BalanceDTO> findAll();


    /**
     * Get the "id" balance.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BalanceDTO> findOne(Long id);

    /**
     * Delete the "id" balance.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
