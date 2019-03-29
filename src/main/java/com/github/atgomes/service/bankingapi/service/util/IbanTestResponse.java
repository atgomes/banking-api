package com.github.atgomes.service.bankingapi.service.util;

public class IbanTestResponse {

    private String message;
    private Integer code;
    private Boolean error;

    public IbanTestResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
