package com.scfg.core.common.enums;

public enum CompanysNitNumber {

    SCVS(370008027), //NIT SCVS
    FBS(1028423022); // NIT FBS

    private int value;

    CompanysNitNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
