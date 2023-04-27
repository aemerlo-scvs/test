package com.scfg.core.domain.dto;

import java.io.Serializable;

public class PolicyDto implements Serializable {

    private static final long serialVersionUID = -1911440404260984563L;
    private float request_id;
    private String request_date;
    private String manager_bank;
    private String names_manager;
    private String policy_number;
    private String emission_date;
    private String date_from;
    private String date_to;
    private String name_plan;
    private float value_am;
    private float value_ac;
    private float premiun_total;
    private String status;
    private long manager_code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRequest_id() {
        return request_id;
    }

    public void setRequest_id(float request_id) {
        this.request_id = request_id;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
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

    public String getPolicy_number() {
        return policy_number;
    }

    public void setPolicy_number(String policy_number) {
        this.policy_number = policy_number;
    }

    public String getEmission_date() {
        return emission_date;
    }

    public void setEmission_date(String emission_date) {
        this.emission_date = emission_date;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
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
