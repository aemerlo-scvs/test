package com.scfg.core.domain.dto;

import java.io.Serializable;

public class ResultPolicyDtO implements Serializable {
    String policy_number;
    String names_manager_correct;
    String names_manager_incorrect;
    Integer manager_code;
    Integer  repitcount;

    public String getPolicy_number() {
        return policy_number;
    }

    public void setPolicy_number(String policy_number) {
        this.policy_number = policy_number;
    }

    public String getNames_manager_correct() {
        return names_manager_correct;
    }

    public void setNames_manager_correct(String names_manager_correct) {
        this.names_manager_correct = names_manager_correct;
    }

    public String getNames_manager_incorrect() {
        return names_manager_incorrect;
    }

    public void setNames_manager_incorrect(String names_manager_incorrect) {
        this.names_manager_incorrect = names_manager_incorrect;
    }

    public Integer getManager_code() {
        return manager_code;
    }

    public void setManager_code(Integer manager_code) {
        this.manager_code = manager_code;
    }

    public Integer getRepitcount() {
        return repitcount;
    }

    public void setRepitcount(Integer repitcount) {
        this.repitcount = repitcount;
    }
}
