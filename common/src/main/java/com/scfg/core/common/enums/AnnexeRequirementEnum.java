package com.scfg.core.common.enums;

public enum AnnexeRequirementEnum {

    RESCUE_REQUEST("Solicitud de Rescate"), // SOLICITUD DE RESCATE
    SETTLEMENT("Finiquito"), // FINIQUITO
    REVERSE_AND_FRONT_CI("Cédula de Identidad"), // CI ANVERSO/REVERSO
    CERTIFICATE_COVERAGE("Certificado de Cobertura"); // CERTIFICADO DE COBERTURA

    private String value;

    AnnexeRequirementEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
