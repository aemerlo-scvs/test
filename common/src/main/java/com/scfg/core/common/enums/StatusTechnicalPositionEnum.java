package com.scfg.core.common.enums;

public enum StatusTechnicalPositionEnum {
    OBSERVED(1), //OBSERVADA
    ACCEPTED(2), //ACEPTADA
    REJECTED(3); //RECHAZADA
    private int value;

    StatusTechnicalPositionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

