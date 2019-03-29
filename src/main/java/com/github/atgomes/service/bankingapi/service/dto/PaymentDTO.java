package com.github.atgomes.service.bankingapi.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the Payment entity.
 */
public class PaymentDTO implements Serializable {

    public static String EXECUTED = "executed";

    private Long id;

    private BigDecimal amount;

    private String currency;

    private String beneficiaryAccountNumber;

    private String beneficiaryName;

    private String communication;

    private Instant creationDate;

    private String status;


    private Long giverAccountId;

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

    public PaymentDTO amount(BigDecimal amount) {
        this.amount=amount;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentDTO currency(String currency) {
        this.currency=currency;
        return this;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public PaymentDTO beneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
        return this;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public PaymentDTO beneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
        return this;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public PaymentDTO communication(String communication) {
        this.communication = communication;
        return this;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public PaymentDTO creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentDTO status(String status) {
        this.status = status;
        return this;
    }

    public Long getGiverAccountId() {
        return giverAccountId;
    }

    public void setGiverAccountId(Long bankAccountId) {
        this.giverAccountId = bankAccountId;
    }

    public PaymentDTO giverAccountId(Long bankAccountId) {
        this.giverAccountId = bankAccountId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (paymentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", beneficiaryAccountNumber='" + getBeneficiaryAccountNumber() + "'" +
            ", beneficiaryName='" + getBeneficiaryName() + "'" +
            ", communication='" + getCommunication() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", giverAccount=" + getGiverAccountId() +
            "}";
    }
}
