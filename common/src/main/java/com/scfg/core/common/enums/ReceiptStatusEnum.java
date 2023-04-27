package com.scfg.core.common.enums;

public enum ReceiptStatusEnum {
    PaidOut(1), //PAGADO
    Canceled(2); //CANCELADO

    private int value;

    ReceiptStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
