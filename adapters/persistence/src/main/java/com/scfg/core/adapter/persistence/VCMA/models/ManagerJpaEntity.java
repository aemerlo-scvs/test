package com.scfg.core.adapter.persistence.VCMA.models;

import com.scfg.core.common.util.HelpersConstants;
import lombok.Builder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = HelpersConstants.TABLE_MANAGER)
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@Builder
public class ManagerJpaEntity implements Serializable {

    @Id
    @Column(name = "manager_id", nullable = false)
    private BigInteger MANAGER_ID;
    @Column(name = "names_ci", nullable = true)
    private String NAMES_CI;
    @Column(name = "names", nullable = false)
    private String NAMES;
    @Column(name = "user_create")
    private BigInteger USER_CREATE;
    @Column(name = "user_update")
    private BigInteger USER_UPDATE;
    @Column(name = "date_create")
    private Date DATE_CREATE;
    @Column(name = "date_update")
    private Date DATE_UPDATE;
    @Column(name = "status")
    private int STATUS;
    @Column(name = "cargo")
    private String CARGO;

    public ManagerJpaEntity() {
    }

    public ManagerJpaEntity(BigInteger MANAGER_ID, String NAMES_CI, String NAMES, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS, String CARGO) {
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

    public ManagerJpaEntity(BigInteger MANAGER_ID, String NAMES, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS, String CARGO) {
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
