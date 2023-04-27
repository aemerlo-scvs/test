package com.scfg.core.common.credentials;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.credential.ad")
public class ActiveDirectoryConfig {
    private String username;
    private String password;
    private String port;
    private String scvsServer;
    private String scvsSearchBase;
    private String bfsServer;
    private String bfsSearchBase;

    public ActiveDirectoryConfig() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getScvsServer() {
        return scvsServer;
    }

    public void setScvsServer(String scvsServer) {
        this.scvsServer = scvsServer;
    }

    public String getScvsSearchBase() {
        return scvsSearchBase;
    }

    public void setScvsSearchBase(String scvsSearchBase) {
        this.scvsSearchBase = scvsSearchBase;
    }

    public String getBfsServer() {
        return bfsServer;
    }

    public void setBfsServer(String bfsServer) {
        this.bfsServer = bfsServer;
    }

    public String getBfsSearchBase() {
        return bfsSearchBase;
    }

    public void setBfsSearchBase(String bfsSearchBase) {
        this.bfsSearchBase = bfsSearchBase;
    }
}
