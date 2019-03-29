package com.github.atgomes.service.bankingapi.service;

import com.github.atgomes.service.bankingapi.service.dto.BankAccountDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing BankAccount.
 */
public interface BankAccountService {

    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save
     * @return the persisted entity
     */
    BankAccountDTO save(BankAccountDTO bankAccountDTO);

    /**
     * Get all the bankAccounts.
     *
     * @return the list of entities
     */
    List<BankAccountDTO> findAll();


    /**
     * Get the "id" bankAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BankAccountDTO> findOne(Long id);

    /**
     * Delete the "id" bankAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    List<BankAccountDTO> getBankAccountsByUserId(Long id);
}
