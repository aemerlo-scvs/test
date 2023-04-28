package com.scfg.core.common.enums;

public enum IntermediaryTypeClassifierEnum {
    DIRECT(1), // DIRECTA
    BROKER(2), // BROKER
    AGENT(3); // AGENTE

    private int value;

    IntermediaryTypeClassifierEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
