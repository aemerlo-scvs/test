package com.scfg.core.common.enums;

public enum SMVSClientTypeEnum {
    EVENTUAL_CLIENT(1), //CLIENTE EVENTUAL
    BANK_CLIENT(2); //CLIENTE DEL BANCO

    private int value;

    SMVSClientTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
