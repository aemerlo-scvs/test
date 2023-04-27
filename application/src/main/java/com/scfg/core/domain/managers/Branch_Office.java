package com.scfg.core.domain.managers;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;


public class Branch_Office implements Serializable {

    private BigInteger BRANCH_OFFICE_ID;
    private String DESCRIPTION;
    private BigInteger USER_CREATE;
    private BigInteger USER_UPDATE;
    private Date DATE_CREATE;
    private Date DATE_UPDATE;
    private int STATUS;

    public Branch_Office() {
    }

    public Branch_Office(BigInteger BRANCH_OFFICE_ID, String DESCRIPTION, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS) {
        this.BRANCH_OFFICE_ID = BRANCH_OFFICE_ID;
        this.DESCRIPTION = DESCRIPTION;
        this.USER_CREATE = USER_CREATE;
        this.USER_UPDATE = USER_UPDATE;
        this.DATE_CREATE = DATE_CREATE;
        this.DATE_UPDATE = DATE_UPDATE;
        this.STATUS = STATUS;
    }

    public BigInteger getBRANCH_OFFICE_ID() {
        return BRANCH_OFFICE_ID;
    }

    public void setBRANCH_OFFICE_ID(BigInteger BRANCH_OFFICE_ID) {
        this.BRANCH_OFFICE_ID = BRANCH_OFFICE_ID;
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
}
