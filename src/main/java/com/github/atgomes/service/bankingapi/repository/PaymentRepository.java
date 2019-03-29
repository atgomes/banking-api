package com.github.atgomes.service.bankingapi.repository;

import com.github.atgomes.service.bankingapi.domain.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "select p.* from payment p inner join customer c on p.giver_account_id=c.id where c.user_id = ?1",
        nativeQuery = true)
    List<Payment> findAllByUserId(Pageable pageable, Long userId);
}
