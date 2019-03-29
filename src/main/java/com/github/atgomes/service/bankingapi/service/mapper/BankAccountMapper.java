package com.github.atgomes.service.bankingapi.service.mapper;

import com.github.atgomes.service.bankingapi.domain.*;
import com.github.atgomes.service.bankingapi.service.dto.BankAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BankAccount and its DTO BankAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface BankAccountMapper extends EntityMapper<BankAccountDTO, BankAccount> {

    @Mapping(source = "customer.id", target = "customerId")
    BankAccountDTO toDto(BankAccount bankAccount);

    @Mapping(target = "balances", ignore = true)
    @Mapping(target = "payments", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    BankAccount toEntity(BankAccountDTO bankAccountDTO);

    default BankAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(id);
        return bankAccount;
    }
}
