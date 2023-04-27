package com.scfg.core.common.enums;

public enum PolicyStatusEnum {
    ACTIVE(1), //VIGENTE
    INMORA(2), //EN MORA
    CANCELED(3), //ANULADAS
    ENDOFVALIDITY(4);//FIN DE VIGENCIA

    private int value;

    PolicyStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

