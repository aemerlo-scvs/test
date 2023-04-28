package com.scfg.core.common.enums;

public enum MathReserveVersionEnum {
    V1("v1"),
    V2("v2");

    private final String value;

    MathReserveVersionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
