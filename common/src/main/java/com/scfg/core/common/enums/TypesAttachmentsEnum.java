package com.scfg.core.common.enums;

public enum TypesAttachmentsEnum {
    DJS(1), // DECLARACION JURADA DE SALUD
    COVERAGECERTIFICATE(2), // CERTIFICADO DE COBERTURA
    SIGNED_DJS(3), // DECLARACION JURADA DE SALUD - FIRMA
    SIGNED_COVERAGECERTIFICATE(4), // CERTIFICADO DE COBERTURA - FIRMA
    PENDING_NOTE(5), // NOTA PENDIENTE
    REJECTED_NOTE(6), // NOTA RECHAZO
    PEPCLIENTDATA(7), // DOCUMENTO PEP ACEPTADO
    CLAUSEDES(8), // CLAUSULA DESEMPLEO
    CLAUSEINC(9), // CLAUSULA DE INVALIDEZ
    REJECTEDPEPCLIENT(10); // DOCUMENTO PEP RECHAZADO

    private int value;

    TypesAttachmentsEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
