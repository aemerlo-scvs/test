package com.scfg.core.domain;

import java.io.Serializable;
import java.sql.Timestamp;


public class PolicyManager implements Serializable {
    private static final long serialVersionUID = 4661606090985058144L;

    private float request_id;
    private Timestamp request_date;
    private String manager_bank;

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String status) {
        this.request_status = status;
    }

    private String names_manager;
    private String request_status;
    private String policy_number;
    private Timestamp emission_date;
    private Timestamp date_from;
    private Timestamp date_to;
    private String name_plan;
    private float value_am;
    private float value_ac;
    private float premiun_total;
    private long manager_code;


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

    public PolicyManager() {
    }

    public PolicyManager(float request_id, Timestamp request_date, String manager_bank, String names_manager, String policy_number, Timestamp emission_date, Timestamp date_from, Timestamp date_to, String name_plan, float value_am, float value_ac, float premiun_total, long manager_code) {

        this.request_id = request_id;
        this.request_date = request_date;
        this.manager_bank = manager_bank;
        this.names_manager = names_manager;
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
}
