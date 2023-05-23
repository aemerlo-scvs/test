package com.scfg.core.common.enums;

public enum CommissionIndicatorClassifierEnum {
    NET_PREMIUM(1), // PRIMA NETA

    TOTAL_PREMIUM(2); // PRIMA TOTAL

    private int value;

    CommissionIndicatorClassifierEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
