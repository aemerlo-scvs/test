package com.scfg.core.common.enums;

public enum PolicySubMovementTypeClassifierEnum {
    NEW(1); // NUEVA

    private int value;

    PolicySubMovementTypeClassifierEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
