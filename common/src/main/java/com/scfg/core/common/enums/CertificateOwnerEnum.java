package com.scfg.core.common.enums;

public enum CertificateOwnerEnum {

    MAGUIRRE("c_maguirre"),

    MFRANCO("c_mfranco"),

    RFMOLINA("c_rfmolina");

    private final String value;

    CertificateOwnerEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
