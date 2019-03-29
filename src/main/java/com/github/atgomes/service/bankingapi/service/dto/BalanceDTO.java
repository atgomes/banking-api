package com.github.atgomes.service.bankingapi.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.github.atgomes.service.bankingapi.domain.enumeration.BalanceType;

/**
 * A DTO for the Balance entity.
 */
public class BalanceDTO implements Serializable {

    private Long id;

    private BigDecimal amount;

    private String currency;

    private BalanceType type;


    private Long bankAccountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BalanceType getType() {
        return type;
    }

    public void setType(BalanceType type) {
        this.type = type;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BalanceDTO balanceDTO = (BalanceDTO) o;
        if (balanceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), balanceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BalanceDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", type='" + getType() + "'" +
            ", bankAccount=" + getBankAccountId() +
            "}";
    }
}
