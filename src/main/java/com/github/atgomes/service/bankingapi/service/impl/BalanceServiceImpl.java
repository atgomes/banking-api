package com.github.atgomes.service.bankingapi.service.impl;

import com.github.atgomes.service.bankingapi.service.BalanceService;
import com.github.atgomes.service.bankingapi.domain.Balance;
import com.github.atgomes.service.bankingapi.repository.BalanceRepository;
import com.github.atgomes.service.bankingapi.service.dto.BalanceDTO;
import com.github.atgomes.service.bankingapi.service.mapper.BalanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Balance.
 */
@Service
@Transactional
public class BalanceServiceImpl implements BalanceService {

    private final Logger log = LoggerFactory.getLogger(BalanceServiceImpl.class);

    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;

    public BalanceServiceImpl(BalanceRepository balanceRepository, BalanceMapper balanceMapper) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
    }

    /**
     * Save a balance.
     *
     * @param balanceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BalanceDTO save(BalanceDTO balanceDTO) {
        log.debug("Request to save Balance : {}", balanceDTO);
        Balance balance = balanceMapper.toEntity(balanceDTO);
        balance = balanceRepository.save(balance);
        return balanceMapper.toDto(balance);
    }

    /**
     * Get all the balances.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<BalanceDTO> findAll() {
        log.debug("Request to get all Balances");
        return balanceRepository.findAll().stream()
            .map(balanceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one balance by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BalanceDTO> findOne(Long id) {
        log.debug("Request to get Balance : {}", id);
        return balanceRepository.findById(id)
            .map(balanceMapper::toDto);
    }

    /**
     * Delete the balance by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Balance : {}", id);
        balanceRepository.deleteById(id);
    }
}
