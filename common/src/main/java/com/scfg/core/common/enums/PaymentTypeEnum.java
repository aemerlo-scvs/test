package com.scfg.core.common.enums;

public enum PaymentTypeEnum {
    Cash(1), //AL CONTADO
    Credit(2); //A CREDITO

    private int value;

    PaymentTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
