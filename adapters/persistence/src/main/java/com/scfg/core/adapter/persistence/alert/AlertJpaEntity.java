package com.scfg.core.adapter.persistence.alert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "ALERT")
public class AlertJpaEntity implements Serializable {
    @Id
    @Column(name = "ALERT_ID")
    private int alert_id;
    @Column(name = "ENVIRONMENT_ID",nullable = false)
    private int environment_id;
    @Column(name = "DESCRIPTION",nullable = false)
    private String description;
    @Column(name = "COUNT_EMAIL",nullable = false)
    private  int count_email;
    @Column(name = "MAIL_BODY",nullable = false)
    private String  mail_body;
    @Column(name = "MAIL_SUBJECT",nullable = false)
    private String mail_subject;
    @Column(name = "MAIL_TO",nullable = false)
    private String mail_to;
    @Column(name = "MAIL_CC",nullable = false)
    private String mail_cc;
    @Column(name = "STATUS",nullable = false)
    private String status;
    @Column(name = "USER_CREATE",nullable = false)
    private int user_create;
    @Column(name = "USER_UPDATE")
    private int user_update;
    @Column(name = "DATE_CREATE",nullable = false)
    private Timestamp date_create;
    @Column(name = "DATE_UPDATE")
    private Timestamp date_update;
    @Column(name = "EXCLUSION_DATE")
    private Timestamp exclusion_date;

    public int getAlert_id() {
        return alert_id;
    }

    public void setAlert_id(int alert_id) {
        this.alert_id = alert_id;
    }

    public int getEnvironment_id() {
        return environment_id;
    }

    public void setEnvironment_id(int environment_id) {
        this.environment_id = environment_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount_email() {
        return count_email;
    }

    public void setCount_email(int count_email) {
        this.count_email = count_email;
    }

    public String getMail_subject() {
        return mail_subject;
    }

    public void setMail_subject(String mail_subject) {
        this.mail_subject = mail_subject;
    }

    public String getMail_to() {
        return mail_to;
    }

    public void setMail_to(String mail_to) {
        this.mail_to = mail_to;
    }

    public String getMail_cc() {
        return mail_cc;
    }

    public void setMail_cc(String mail_cc) {
        this.mail_cc = mail_cc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser_create() {
        return user_create;
    }

    public void setUser_create(int user_create) {
        this.user_create = user_create;
    }

    public int getUser_update() {
        return user_update;
    }

    public void setUser_update(int user_update) {
        this.user_update = user_update;
    }

    public Timestamp getDate_create() {
        return date_create;
    }

    public void setDate_create(Timestamp date_create) {
        this.date_create = date_create;
    }

    public Timestamp getDate_update() {
        return date_update;
    }

    public void setDate_update(Timestamp date_update) {
        this.date_update = date_update;
    }

    public Timestamp getExclusion_date() {
        return exclusion_date;
    }

    public void setExclusion_date(Timestamp exclusion_date) {
        this.exclusion_date = exclusion_date;
    }

    public String getMail_body() {
        return mail_body;
    }

    public void setMail_body(String mail_body) {
        this.mail_body = mail_body;
    }
}
