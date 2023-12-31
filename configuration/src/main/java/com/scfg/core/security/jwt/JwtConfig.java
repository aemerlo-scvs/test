package com.scfg.core.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
    private Integer tokenExpirationAfterMilliseconds;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }

    public Integer getTokenExpirationAfterMilliseconds() {
        return tokenExpirationAfterMilliseconds;
    }

    public void setTokenExpirationAfterMilliseconds(Integer tokenExpirationAfterMilliseconds) {
        this.tokenExpirationAfterMilliseconds = tokenExpirationAfterMilliseconds;
    }

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }

}
