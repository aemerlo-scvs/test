package com.scfg.core.common.enums;

public enum MonthlyDisbursementCaseStatusEnum {

    PendingValidation(0, "Pendiente de Validacion"),
    CaseInOrden(1,"Caso en regla"),
    CaseObserved(-1, "Caso observado");

    private int identifier;
    private String description;

    MonthlyDisbursementCaseStatusEnum(int identifier, String description){
        this.identifier = identifier;
        this.description = description;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }
}
