package com.scfg.core.adapter.persistence.VCMA.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "KNOWLEDGE_HISTORY")
public class Knowledge_HistoryJpaEntity {
    @Id
    @Column(name = "POLICY_NUMBER")
    private  String policy_number;
    @Column(name = "NAMES_INCORRECT", nullable = false)
    private String names_incorrect;
    @Column(name = "NAMES_CORRECT")
    private String names_correct;
    @Column(name = "MANAGER_CODE")
    private float manager_code;

    public Knowledge_HistoryJpaEntity(String policy_history, String names_incorrect, String names_correct, float manager_code) {
        this.policy_number = policy_history;
        this.names_incorrect = names_incorrect;
        this.names_correct = names_correct;
        this.manager_code = manager_code;
    }
    public Knowledge_HistoryJpaEntity() {

    }

    public String getPolicy_number() {
        return policy_number;
    }

    public void setPolicy_number(String policy_number) {
        this.policy_number = policy_number;
    }

    public String getNames_incorrect() {
        return names_incorrect;
    }

    public void setNames_incorrect(String names_incorrect) {
        this.names_incorrect = names_incorrect;
    }

    public String getNames_correct() {
        return names_correct;
    }

    public void setNames_correct(String names_correct) {
        this.names_correct = names_correct;
    }

    public float getManager_code() {
        return manager_code;
    }

    public void setManager_code(float manager_code) {
        this.manager_code = manager_code;
    }
}
