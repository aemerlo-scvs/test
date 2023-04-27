package com.scfg.core.common.enums;

public enum ProductAgreementCodeEnum {
    SMVS(746),
    VIN(1083);

    private int value;

    ProductAgreementCodeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
