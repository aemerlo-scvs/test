package com.scfg.core.common.enums;

public enum PeriodicityEnum {
    None(0), //NINGUNA
    Annual(1), //ANUAL
    SemiAnnual(2); //SEMESTRAL

    private int value;

    PeriodicityEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
