package com.scfg.core.common.enums;

public enum ProductEnum {
    SMVS("SMVS"),
    DHN("DHN"),
    VIN("VIN");

    private String value;

    ProductEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
