package com.github.atgomes.service.bankingapi.service.util;

import java.math.BigDecimal;

public class PaymentTentative {

    private String giverAccount;
    private BigDecimal amount;
    private String currency;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String communication;

    public PaymentTentative() {
    }

    public String getGiverAccount() {
        return giverAccount;
    }

    public void setGiverAccount(String giverAccount) {
        this.giverAccount = giverAccount;
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

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }
}
