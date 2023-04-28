package com.scfg.core.common.enums;

public enum RequestAnnexeStatusEnum {
    PENDING(1),
    REQUESTED(2),
    ACCEPTED(3),
    OBSERVED(4),
    REJECTED(5),
    PAID(6);

    private int value;

    RequestAnnexeStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

