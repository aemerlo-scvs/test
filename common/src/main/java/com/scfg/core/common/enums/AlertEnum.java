package com.scfg.core.common.enums;

public enum AlertEnum {

    WELCOME(1), // Mensaje de Bienvenida
    ACTIVATION(2), // Mensaje de Activación
    SCHEDULED_CLIENT_MESSAGE(3), // Mensaje Programado para el cliente
    SCHEDULED_CONTACT_CENTER_MESSAGE(4), // Mensaje Programado para el contact center
    MAKE_PAYMENT_ALERT(5), //Mensaje de Alerta si el pago de sepelio no puede ser completado
    SCHEDULED_REPORT_COMMERCIALS(6), //REPORTE COMERCIAL
    GEL_TECHO_PENDING_REQUEST(7), //Envio de correo para pendiente de GEL
    GEL_TECHO_PEP_REQUEST(8), //Envio de correo para PEP de GEL
    GEL_TECHO_REJECTED_REQUEST(9), //Envio de correo para rechazo de GEL
    GEL_TECHO_APPROVED_REQUEST(10), //Envio de correo para aceptado de GEL
    GEL_CASAMIA_PENDING_REQUEST(11), //Envio de correo para pendiente de GEL
    GEL_CASAMIA_PEP_REQUEST(12), //Envio de correo para PEP de GEL
    GEL_CASAMIA_REJECTED_REQUEST(13), //Envio de correo para rechazo de GEL
    GEL_CASAMIA_APPROVED_REQUEST(14),
    GEL_PAHUICHI_PENDING_REQUEST(16), //Envio de correo para pendiente de GEL
    GEL_PAHUICHI_PEP_REQUEST(17), //Envio de correo para PEP de GEL
    GEL_PAHUICHI_REJECTED_REQUEST(18), //Envio de correo para rechazo de GEL
    GEL_PAHUICHI_APPROVED_REQUEST(19),//Envio de correo para aceptado de GEL; //Envio de correo para aceptado de GEL
    SMVS_WELCOME_MESSAGE(20),//Envio de correo de bienvenida - SMVS
    SMVS_PAYMENT_ALERT(21),//Envio de correo de pendiente activacion - SMVS
    SMVS_RECORDATORY(22),//Envio de correo de recordatorio activacion - SMVS
    SMVS_ACTIVATION(23),//Envio de correo de activacion - SMVS

    SMVS_SCHEDULED_CLIENT_MESSAGE(24),//Envio de correo con excel de reporte de clientes con solicitudes pendientes
    VIN_ACTIVATION_CONFIRM_PROPOSAL(25),//Envio de correo de propuesta para su aceptación del cliente - VIN - EMAIL
    VIN_CERTIFICATE_SEND(26), //Envío del certificado - VIN
    VIN_ACCEPT_PROP(27), //Aceptación de propuesta - VIN
    VIN_REJECT_PROP(28), //Rechazo de propuesta - VIN
    VIN_ACTIVATION_CONFIRM_PROPOSAL_SMS(29),//Envio de correo de propuesta para su aceptación del cliente - VIN - SMSN
    VIN_REQUESTANNEXE_ACEPTED(30), //Aceptacion de propuesta - VIN
    VIN_REQUESTANNEXE_OBSERVED(31), //Observacion de propuesta - VIN
    VIN_REQUESTANNEXE_REJECTED(32), //Rechazo de propuesta - VIN
    VIN_ANNEXE_CONFIRM_PAYMENT_VOUCHER(33), //Comprobande de pago de propuesta - VIN
    VIN_REQUESTANNEXE_ACEPTED_ACCOUTING(34); //Contabilidad - VIN

    private int value;

    AlertEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
