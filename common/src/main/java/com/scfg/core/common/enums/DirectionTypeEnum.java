package com.scfg.core.common.enums;

public enum DirectionTypeEnum {
    PERSONAL(1), // Domicilio
    WORK(2), // Trabajo
    COMMERCIAL(3); // Comercial

    private int value;

    DirectionTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
