package com.scfg.core.common.enums;

public enum GelCompanyEnum {
    TECHO("TECHO"),
    PAHUICHI("PAHUICHI"),
    CASAMIA("CASA");

    private String value;

    GelCompanyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
