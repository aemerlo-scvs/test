package com.scfg.core.common.enums;

public enum AdConfigEnum {
    AD_DOMAIN("_domain"),

    AD_PORT("_port"),

    AD_SEARCH_BASE("_searchBase");

    private String value;

    AdConfigEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
