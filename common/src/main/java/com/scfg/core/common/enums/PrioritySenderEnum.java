package com.scfg.core.common.enums;

public enum PrioritySenderEnum {
    FIRST(1), //PRIMER ENVIO DE RECORDATORIO C-10
    SECOND(2), //SEGUNDO ENVIO DE RECORDATORIO C-1
    THIRD(3), //TERCER ENVIO DE RECORDATORIO C-VENCIDA
    FOUR(4),//CUARTO ENVIO DE RECORDATORIO C+10
    FIFTH(5);//QUINTO ENVIO DE RECORDATORIO C+10+10

    private int value;

    PrioritySenderEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
