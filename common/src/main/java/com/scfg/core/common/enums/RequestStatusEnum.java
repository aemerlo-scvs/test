package com.scfg.core.common.enums;

public enum RequestStatusEnum {
    PENDING(1), //PENDIENTE
    FINALIZED(2), //FINALIZADO
    REJECTED(3), //RECHAZADO
    CANCELLED(4), //CANCELADO
    POSTPONED(5), //POSPUESTO
    INACTIVE(6); //INACTIVO
    private int value;

    RequestStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
