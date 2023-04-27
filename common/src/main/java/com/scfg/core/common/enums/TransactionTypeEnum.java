package com.scfg.core.common.enums;

public enum TransactionTypeEnum {
    PremiumPayment(1), //PAGO DE PRIMA
    PremiumRefund(2), //DEVOLUCIÓN DE PRIMA
    ClaimPayment(3); //PAGO DE SINIESTRO

    private int value;

    TransactionTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
