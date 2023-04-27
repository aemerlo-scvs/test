package com.scfg.core.adapter.persistence.VCMA.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name="BRANCH_OFFICE")
public class Branch_OfficeJpaEntity implements Serializable {

    @Id
    @Column(name = "branch_office_id", nullable = false)
    private BigInteger BRANCH_OFFICE_ID;
    @Column(name = "description", nullable = false)
    private String DESCRIPTION;
    @Column(name = "user_create", nullable = true)
    private BigInteger USER_CREATE;
    @Column(name = "user_update", nullable = true)
    private BigInteger USER_UPDATE;
    @Column(name = "date_create", nullable = true)
    private Date DATE_CREATE;
    @Column(name = "date_update", nullable = true)
    private Date DATE_UPDATE;
    @Column(name = "status", nullable = true)
    private int STATUS;

    public Branch_OfficeJpaEntity() {
    }

    public Branch_OfficeJpaEntity(BigInteger BRANCH_OFFICE_ID, String DESCRIPTION, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS) {
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
