package com.scfg.core.common.enums;

public enum PendingTypeEnum {
    PENDING_FOR_IMC("Índice de masa corporal observado",1),
    PENDING_FOR_CUMULUS("CUMULO MAYOR AL LIMITE PERMITIDO",2),
    PENDING_FOR_HAVE_OBS_COMPANY_REQUEST("ANTECEDENTES EN LA COMPAÑIA",3),
    PENDING_FOR_CUMULUS_PEP("CUMULO MAYOR AL LIMITE PERMITIDO POR CUMPLIMIENTO",4),
    PENDING_FOR_MEDIC_REQUIREMENTS("POR CUMPLIMIENTO DE REQUISITOS MÉDICOS",5),

    PENDING_FOR_AFFIRMATIVE_DJS("POR PREGUNTAS MÉDICAS POSITIVAS",6);
    private String action;
    private int identifier;

    public int getIdentifier() {
        return identifier;
    }

    public String getAction() {
        return action;
    }

    PendingTypeEnum(String action, int identifier) {
        this.action = action;
        this.identifier = identifier;
    }
}
