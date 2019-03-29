package com.github.atgomes.service.bankingapi.service.mapper;

import com.github.atgomes.service.bankingapi.domain.*;
import com.github.atgomes.service.bankingapi.service.dto.BalanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Balance and its DTO BalanceDTO.
 */
@Mapper(componentModel = "spring", uses = {BankAccountMapper.class})
public interface BalanceMapper extends EntityMapper<BalanceDTO, Balance> {

    @Mapping(source = "bankAccount.id", target = "bankAccountId")
    BalanceDTO toDto(Balance balance);

    @Mapping(source = "bankAccountId", target = "bankAccount")
    Balance toEntity(BalanceDTO balanceDTO);

    default Balance fromId(Long id) {
        if (id == null) {
            return null;
        }
        Balance balance = new Balance();
        balance.setId(id);
        return balance;
    }
}
