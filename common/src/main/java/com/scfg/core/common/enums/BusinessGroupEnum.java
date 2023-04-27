package com.scfg.core.common.enums;

public enum BusinessGroupEnum {
    CREDICASAS(1),
    PAZ(2),
    SCVS(3),
    BFS(4);

    private int value;

    BusinessGroupEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
