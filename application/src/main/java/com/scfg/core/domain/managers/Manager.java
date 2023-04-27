package com.scfg.core.domain.managers;

import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@SuperBuilder
public class Manager implements Serializable {

    private BigInteger MANAGER_ID;
    private String NAMES_CI;
    private String NAMES;
    private BigInteger USER_CREATE;
    private BigInteger USER_UPDATE;
    private Date DATE_CREATE;
    private Date DATE_UPDATE;
    private int STATUS;
    private String CARGO;

    public Manager() {
    }

    public Manager(BigInteger MANAGER_ID, String NAMES_CI, String NAMES, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS, String CARGO) {
        this.MANAGER_ID = MANAGER_ID;
        this.NAMES_CI = NAMES_CI;
        this.NAMES = NAMES;
        this.USER_CREATE = USER_CREATE;
        this.USER_UPDATE = USER_UPDATE;
        this.DATE_CREATE = DATE_CREATE;
        this.DATE_UPDATE = DATE_UPDATE;
        this.STATUS = STATUS;
        this.CARGO = CARGO;
    }

    public Manager(BigInteger MANAGER_ID, String NAMES, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS, String CARGO) {
        this.MANAGER_ID = MANAGER_ID;
        this.NAMES = NAMES;
        this.USER_CREATE = USER_CREATE;
        this.USER_UPDATE = USER_UPDATE;
        this.DATE_CREATE = DATE_CREATE;
        this.DATE_UPDATE = DATE_UPDATE;
        this.STATUS = STATUS;
        this.CARGO = CARGO;
    }

    public BigInteger getMANAGER_ID() {
        return MANAGER_ID;
    }

    public void setMANAGER_ID(BigInteger MANAGER_ID) {
        this.MANAGER_ID = MANAGER_ID;
    }

    public String getNAMES_CI() {
        return NAMES_CI;
    }

    public void setNAMES_CI(String NAMES_CI) {
        this.NAMES_CI = NAMES_CI;
    }

    public String getNAMES() {
        return NAMES;
    }

    public void setNAMES(String NAMES) {
        this.NAMES = NAMES;
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

    public String getCARGO() {
        return CARGO;
    }

    public void setCARGO(String CARGO) {
        this.CARGO = CARGO;
    }


}
