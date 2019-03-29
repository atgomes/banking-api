package com.github.atgomes.service.bankingapi.repository;

import com.github.atgomes.service.bankingapi.domain.BankAccount;
import com.github.atgomes.service.bankingapi.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "select * from customer c where c.user_id = ?1",
        nativeQuery = true)
    Optional<Customer> findCustomerByUserId(Long userId);
}
