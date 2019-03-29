package com.github.atgomes.service.bankingapi.service.dto;
import com.github.atgomes.service.bankingapi.domain.BankAccount;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Customer entity.
 */
public class CustomerDTO implements Serializable {

    private Long id;

    private String address;

    private Long userId;

    private Set<Long> bankAccountIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Long> getBankAccountIds() {
        return bankAccountIds;
    }

    public void setBankAccountIds(Set<Long> bankAccountIds) {
        this.bankAccountIds = bankAccountIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (customerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", user=" + getUserId() +
            "}";
    }
}
