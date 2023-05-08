package com.scfg.core.common.enums;

public enum AnnulmentRequestAnnexeDocumentTypeClassifierEnum {
    RESCUE_REQUEST(1), // SOLICITUD DE RESCATE
    SETTLEMENT(2), // FINIQUITO
    REVERSE_AND_FRONT_CI(3), // CI ANVERSO/REVERSO
    RESCUE_NOTE(4), // COMUNICADO DE RESCATE
    PROOF_OF_PAYMENT(5); // COMPROBANTE DE PAGO

    private int value;

    AnnulmentRequestAnnexeDocumentTypeClassifierEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
