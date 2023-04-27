package com.scfg.core.common.enums;

public enum PaymentChannelEnum {
    Cash(1), //EFECTIVO
    Check(2), //CHEQUE
    Debit(3), //DEBITO
    Transfer(4); //TRANSFERENCIA

    private int value;

    PaymentChannelEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
