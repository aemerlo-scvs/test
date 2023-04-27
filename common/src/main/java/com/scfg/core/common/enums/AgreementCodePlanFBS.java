package com.scfg.core.common.enums;

public enum AgreementCodePlanFBS {
    VIN_CODE(1); //PLAN VIN - FBS

    private int value;

    AgreementCodePlanFBS(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
