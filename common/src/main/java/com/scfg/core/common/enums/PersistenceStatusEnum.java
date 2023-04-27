package com.scfg.core.common.enums;

public enum PersistenceStatusEnum {

    CREATED_OR_UPDATED(1),
    DELETED(0);

    private int value;

    PersistenceStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
