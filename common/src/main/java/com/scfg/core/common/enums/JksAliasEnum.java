package com.scfg.core.common.enums;

public enum JksAliasEnum {
    CERT_CONTENT("c_content"),

    CERT_PASSWORD("c_password");

    private final String value;

    JksAliasEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
