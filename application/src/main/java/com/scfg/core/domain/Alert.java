package com.scfg.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Alert {
    private int alert_id;
    private int environment_id;
    private String description;
    private int count_email;
    private String mail_body;
    private String mail_subject;
    private String mail_to;
    private String mail_cc;
    private String status;
    private int user_create;
    private int user_update;
    private Timestamp date_create;
    private Timestamp date_update;
    private Timestamp exclusion_date;

    public void setSubjectAndBody (String mail_subject, String mail_body) {
        this.mail_subject = mail_subject;
        this.mail_body = mail_body;
    }
}
