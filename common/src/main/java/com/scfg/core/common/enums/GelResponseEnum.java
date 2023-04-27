package com.scfg.core.common.enums;

public enum GelResponseEnum {
    PENDING_MESSAGE("La solicitud del cliente está en proceso de revisión por parte de la compañía Santa Cruz Vida y Salud."),
    REJECTED_MESSAGE("Solicitud rechazada por edad o IMC. Revisar la Declaración Jurada de Salud que se descargó automáticamente."),
    ACCEPTED_MESSAGE("Solicitud aceptada. Revisar el Certificado de Cobertura y la Declaración Jurada de Salud que se descargaron automáticamente.");

    private String value;

    GelResponseEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
