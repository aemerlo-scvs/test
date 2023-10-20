package com.scfg.core.common.credentials;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.credential.twilio")
public class TwilioConfig {

    private String accountSID;
    private String authToken;
    private String cellphoneNumber;
    private String whatsAppNumber;
    private String whatsAppNumber_sandBox;

    public TwilioConfig() {
    }

    public String getAccountSID() {
        return accountSID;
    }

    public void setAccountSID(String accountSID) {
        this.accountSID = accountSID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getWhatsAppNumber() {
        return whatsAppNumber;
    }

    public void setWhatsAppNumber(String whatsAppNumber) {
        this.whatsAppNumber = whatsAppNumber;
    }

    public String getWhatsAppNumber_sandBox() {
        return whatsAppNumber_sandBox;
    }

    public void setWhatsAppNumber_sandBox(String whatsAppNumber_sandBox) {
        this.whatsAppNumber_sandBox = whatsAppNumber_sandBox;
    }
}
