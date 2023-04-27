package com.scfg.core.common.enums;

public enum GelAlertEnum {
    PENDING_REQUEST(1), //Envio de correo para pendiente de GEL
    PEP_REQUEST(2), //Envio de correo para PEP de GEL
    REJECTED_REQUEST(3), //Envio de correo para rechazo de GEL
    APPROVED_REQUEST(4); //Envio de correo para aprobación de GEL

    private int value;

    GelAlertEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
