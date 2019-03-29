package com.github.atgomes.service.bankingapi.repository;

import com.github.atgomes.service.bankingapi.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
