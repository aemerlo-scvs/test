package com.scfg.core.common.enums;

public enum TypesDocumentPersonEnum {
    FRONTCARD(1), //CARNET ANVERSO (PNG,JPG, JPGE)
    REVERSECARD(2), //CARNET REVERSO (PNG,JPG, JPGE)
    REVERSEANDFRONTCARD(3), //CARNET ANVERSO/REVERSO (PDF)
    DIGITALFIRM(4),//FIN DE VIGENCIA(PNG)
    LOGOPJ(5);//LOGO PARA PERSONAS JURIDICAS

    private int value;

    TypesDocumentPersonEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
