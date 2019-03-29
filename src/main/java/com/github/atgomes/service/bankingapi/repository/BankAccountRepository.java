package com.github.atgomes.service.bankingapi.repository;

import com.github.atgomes.service.bankingapi.domain.BankAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the BankAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query(value = "select b.* from bank_account b inner join customer c on b.customer_id=c.id where c.user_id = ?1",
        nativeQuery = true)
    List<BankAccount> findBankAccountsByUserId(Long userId);
}
