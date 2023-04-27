package com.scfg.core.common.credentials;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.credential.smvs.recaptcha")
public class SMVSReCaptchaConfig {
    private String secretKey;

    public SMVSReCaptchaConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
