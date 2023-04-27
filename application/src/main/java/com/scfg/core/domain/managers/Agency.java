package com.scfg.core.domain.managers;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@SuperBuilder
public class Agency implements Serializable {

    private BigInteger AGENCY_ID;
    private String DESCRIPTION;
    private BigInteger USER_CREATE;
    private BigInteger USER_UPDATE;
    private Date DATE_CREATE;
    private Date DATE_UPDATE;
    private int STATUS;
    private BigInteger BRANCH_OFFICE_ID;
    private Long ZONES_ID;

    public Agency() {
    }

    public Agency(BigInteger AGENCY_ID, String DESCRIPTION, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS, BigInteger BRANCH_OFFICE_ID, Long ZONES_ID) {
        this.AGENCY_ID = AGENCY_ID;
        this.DESCRIPTION = DESCRIPTION;
        this.USER_CREATE = USER_CREATE;
        this.USER_UPDATE = USER_UPDATE;
        this.DATE_CREATE = DATE_CREATE;
        this.DATE_UPDATE = DATE_UPDATE;
        this.STATUS = STATUS;
        this.BRANCH_OFFICE_ID = BRANCH_OFFICE_ID;
        this.ZONES_ID = ZONES_ID;
    }

    public BigInteger getAGENCY_ID() {
        return AGENCY_ID;
    }

    public void setAGENCY_ID(BigInteger AGENCY_ID) {
        this.AGENCY_ID = AGENCY_ID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public BigInteger getUSER_CREATE() {
        return USER_CREATE;
    }

    public void setUSER_CREATE(BigInteger USER_CREATE) {
        this.USER_CREATE = USER_CREATE;
    }

    public BigInteger getUSER_UPDATE() {
        return USER_UPDATE;
    }

    public void setUSER_UPDATE(BigInteger USER_UPDATE) {
        this.USER_UPDATE = USER_UPDATE;
    }

    public Date getDATE_CREATE() {
        return DATE_CREATE;
    }

    public void setDATE_CREATE(Date DATE_CREATE) {
        this.DATE_CREATE = DATE_CREATE;
    }

    public Date getDATE_UPDATE() {
        return DATE_UPDATE;
    }

    public void setDATE_UPDATE(Date DATE_UPDATE) {
        this.DATE_UPDATE = DATE_UPDATE;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public BigInteger getBRANCH_OFFICE_ID() {
        return BRANCH_OFFICE_ID;
    }

    public void setBRANCH_OFFICE_ID(BigInteger BRANCH_OFFICE_ID) {
        this.BRANCH_OFFICE_ID = BRANCH_OFFICE_ID;
    }

    public Long getZONES_ID() {
        return ZONES_ID;
    }

    public void setZONES_ID(Long ZONES_ID) {
        this.ZONES_ID = ZONES_ID;
    }
}
