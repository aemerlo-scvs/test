package com.scfg.core.common.enums;

public enum PolicyTypeOptionEnum {

    PolicyDHL(1),
    PolicyDHN(2);

    private long identifier;

    public long getIdentifier() {
        return identifier;
    }

    PolicyTypeOptionEnum(long identifier) {
        this.identifier = identifier;
    }
}
