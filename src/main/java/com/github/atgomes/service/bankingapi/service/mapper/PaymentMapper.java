package com.github.atgomes.service.bankingapi.service.mapper;

import com.github.atgomes.service.bankingapi.domain.*;
import com.github.atgomes.service.bankingapi.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Payment and its DTO PaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {BankAccountMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "giverAccount.id", target = "giverAccountId")
    PaymentDTO toDto(Payment payment);

    @Mapping(source = "giverAccountId", target = "giverAccount")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
