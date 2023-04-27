package com.scfg.core.common.enums;

public enum AnnexedRequirementEnum {

    REQ_RESCUE("Solicitud de rescate"), //Rescate
    REQ_FINIQUITO("Finiquito"), //Finiquito
    REQ_CI("Cédula de Identidad"); //CI

    private String value;

    AnnexedRequirementEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
