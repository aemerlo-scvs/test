package com.scfg.core.common.enums;

public enum SMVSMessageTypeEnum {
    WELCOME(1), // Mensaje de Bienvenida
    ACTIVATION(2), // Mensaje de Activación
    SCHEDULED_CLIENT_MESSAGE(3), // Mensaje Programado para el cliente
    SCHEDULED_CONTACT_CENTER_MESSAGE(4), // Mensaje Programado para el contact center
    MAKE_PAYMENT_ALERT(5), //Mensaje de Alerta si el pago de sepelio no puede ser completado
    SCHEDULED_REPORT_COMMERCIALS(6), //REPORTE COMERCIAL
    SYSTEM_ACTIVATE_AUTO(15); //CORREO PARA GENERACIÓN AUTOMATICA DE SISTEMA, POLIZAS PENDIENTES SEPELIO

    private int value;

    SMVSMessageTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
