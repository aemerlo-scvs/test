package com.scfg.core.common.enums;

public enum CompanyInitialEnum {
    SCVS("scvs"),

    BFS("bfs");

    private String value;

    CompanyInitialEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
