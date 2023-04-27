package com.scfg.core.common.enums;

public enum CLFResponseEnum {
    ACCEPTED(1),
    PENDING(2),
    REJECTED(3);

    private int value;

    CLFResponseEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
