package com.scfg.core.adapter.persistence.VCMA.models;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name="ZONES")
public class ZoneJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zones_id", nullable = false)
    private Long ZONES_ID;
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

    public ZoneJpaEntity() {
    }

    public ZoneJpaEntity(String DESCRIPTION, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS) {
        this.DESCRIPTION = DESCRIPTION;
        this.USER_CREATE = USER_CREATE;
        this.USER_UPDATE = USER_UPDATE;
        this.DATE_CREATE = DATE_CREATE;
        this.DATE_UPDATE = DATE_UPDATE;
        this.STATUS = STATUS;
    }

    public ZoneJpaEntity(Long ZONES_ID, String DESCRIPTION, BigInteger USER_CREATE, BigInteger USER_UPDATE, Date DATE_CREATE, Date DATE_UPDATE, int STATUS) {
        this.ZONES_ID = ZONES_ID;
        this.DESCRIPTION = DESCRIPTION;
        this.USER_CREATE = USER_CREATE;
        this.USER_UPDATE = USER_UPDATE;
        this.DATE_CREATE = DATE_CREATE;
        this.DATE_UPDATE = DATE_UPDATE;
        this.STATUS = STATUS;
    }

    public Long getZONES_ID() {
        return ZONES_ID;
    }

    public void setZONES_ID(Long ZONES_ID) {
        this.ZONES_ID = ZONES_ID;
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
