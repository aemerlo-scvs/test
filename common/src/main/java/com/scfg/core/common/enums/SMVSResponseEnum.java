package com.scfg.core.common.enums;

public enum SMVSResponseEnum {
    OK(1),
    ERROR(2);

    private int value;

    SMVSResponseEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
