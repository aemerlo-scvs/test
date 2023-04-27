package com.scfg.core.adapter.persistence.VCMA.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "POLICY_MANAGER")
public class PolicyManagerJpaEntity {
    private static final long serialVersionUID = 4661606090985058144L;

    @Column(name = "REQUEST_ID", nullable = true)
    private float request_id;
    @Column(name = "REQUEST_DATE", nullable = true)
    private Timestamp request_date;
    @Column(name = "MANAGER_BANK", nullable = true)
    private String manager_bank;
    @Column(name = "NAMES_MANAGER", nullable = false)
    private String names_manager;
    @Column(name = "REQUEST_STATUS", nullable = false)
    private String request_status;
    @Id
    @Column(name = "POLICY_NUMBER")
    private String policy_number;
    @Column(name = "EMISSION_DATE", nullable = false)
    private Timestamp emission_date;
    @Column(name = "DATE_FROM", nullable = false)
    private Timestamp date_from;
    @Column(name = "DATE_TO", nullable = false)
    private Timestamp date_to;
    @Column(name = "NAME_PLAN", nullable = false)
    private String name_plan;
    @Column(name = "VALUE_AM", nullable = false)
    private float value_am;
    @Column(name = "VALUE_AC", nullable = false)
    private float value_ac;
    @Column(name = "PREMIUN_TOTAL", nullable = false)
    private float premiun_total;
    @Column(name = "MANAGER_CODE", nullable = false)
    private long manager_code;

    public PolicyManagerJpaEntity() {
    }

    public PolicyManagerJpaEntity(float request_id, Timestamp request_date, String manager_bank, String names_manager, String request_status, String policy_number, Timestamp emission_date, Timestamp date_from, Timestamp date_to, String name_plan, float value_am, float value_ac, float premiun_total, long manager_code) {
        this.request_id = request_id;
        this.request_date = request_date;
        this.manager_bank = manager_bank;
        this.names_manager = names_manager;
        this.request_status = request_status;
        this.policy_number = policy_number;
        this.emission_date = emission_date;
        this.date_from = date_from;
        this.date_to = date_to;
        this.name_plan = name_plan;
        this.value_am = value_am;
        this.value_ac = value_ac;
        this.premiun_total = premiun_total;
        this.manager_code = manager_code;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public float getRequest_id() {
        return request_id;
    }

    public void setRequest_id(float request_id) {
        this.request_id = request_id;
    }

    public Timestamp getRequest_date() {
        return request_date;
    }

    public void setRequest_date(Timestamp request_date) {
        this.request_date = request_date;
    }

    public String getManager_bank() {
        return manager_bank;
    }

    public void setManager_bank(String manager_bank) {
        this.manager_bank = manager_bank;
    }

    public String getNames_manager() {
        return names_manager;
    }

    public void setNames_manager(String names_manager) {
        this.names_manager = names_manager;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    public String getPolicy_number() {
        return policy_number;
    }

    public void setPolicy_number(String policy_number) {
        this.policy_number = policy_number;
    }

    public Timestamp getEmission_date() {
        return emission_date;
    }

    public void setEmission_date(Timestamp emission_date) {
        this.emission_date = emission_date;
    }

    public Timestamp getDate_from() {
        return date_from;
    }

    public void setDate_from(Timestamp date_from) {
        this.date_from = date_from;
    }

    public Timestamp getDate_to() {
        return date_to;
    }

    public void setDate_to(Timestamp date_to) {
        this.date_to = date_to;
    }

    public String getName_plan() {
        return name_plan;
    }

    public void setName_plan(String name_plan) {
        this.name_plan = name_plan;
    }

    public float getValue_am() {
        return value_am;
    }

    public void setValue_am(float value_am) {
        this.value_am = value_am;
    }

    public float getValue_ac() {
        return value_ac;
    }

    public void setValue_ac(float value_ac) {
        this.value_ac = value_ac;
    }

    public float getPremiun_total() {
        return premiun_total;
    }

    public void setPremiun_total(float premiun_total) {
        this.premiun_total = premiun_total;
    }

    public long getManager_code() {
        return manager_code;
    }

    public void setManager_code(long manager_code) {
        this.manager_code = manager_code;
    }
}
