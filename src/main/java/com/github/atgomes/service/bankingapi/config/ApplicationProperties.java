package com.github.atgomes.service.bankingapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Banking API.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String ibanTestApiKey;
    private String ibanTestApiUrl;

    public String getIbanTestApiKey() {
        return ibanTestApiKey;
    }

    public void setIbanTestApiKey(String ibanTestApiKey) {
        this.ibanTestApiKey = ibanTestApiKey;
    }

    public String getIbanTestApiUrl() {
        return ibanTestApiUrl;
    }

    public void setIbanTestApiUrl(String ibanTestApiUrl) {
        this.ibanTestApiUrl = ibanTestApiUrl;
    }
}
