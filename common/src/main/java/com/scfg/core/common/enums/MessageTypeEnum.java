package com.scfg.core.common.enums;

public enum MessageTypeEnum {
    EMAIL(1),
    WHATSAPP(2),
    SMS(3);

    private final int value;

    MessageTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
