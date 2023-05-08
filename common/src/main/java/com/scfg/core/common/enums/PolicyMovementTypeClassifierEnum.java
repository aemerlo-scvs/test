package com.scfg.core.common.enums;

public enum PolicyMovementTypeClassifierEnum {
    PRODUCTION(1), // PRODUCCIÓN
    ANNULMENT(2); // ANULACIÓN

    private int value;

    PolicyMovementTypeClassifierEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
