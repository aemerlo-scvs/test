package com.scfg.core.common.enums;

public enum APSCodesEnum {

    APS_PROPOSAL_VIN_CODE("212-934124-2023 01 017 3001"), //COD. APS PARA PROPUESTA VIN
    APS_CERT_VIN_CODE("212-934124-2023 01 017 4001"); //COD. APS CERT. VIN

    private String value;

    APSCodesEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
