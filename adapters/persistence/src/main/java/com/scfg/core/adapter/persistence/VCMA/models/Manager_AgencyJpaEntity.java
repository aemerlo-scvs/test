package com.scfg.core.adapter.persistence.VCMA.models;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name="MANAGER_AGENCY")
public class Manager_AgencyJpaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_agency_id", nullable = false)
    private Long MANAGER_AGENCY_ID;
    @Column(name = "agency_id", nullable = false)
    private BigInteger AGENCY_ID;
    @Column(name = "manager_id", nullable = false)
    private BigInteger MANAGER_ID;
    @Column(name = "user_create", nullable = true)
    private BigInteger USER_CREATE;
    @Column(name = "date_create", nullable = true)
    private Date DATE_CREATE;
    @Column(name = "inclusion_date", nullable = true)
    private Date INCLUSION_DATE;
    @Column(name = "exclusion_date", nullable = true)
    private Date EXCLUSION_DATE;
    @Column(name = "status", nullable = true)
    private int STATUS;

    public Manager_AgencyJpaEntity() {
    }

    public Manager_AgencyJpaEntity(Long MANAGER_AGENCY_ID, BigInteger AGENCY_ID, BigInteger MANAGER_ID, BigInteger USER_CREATE, Date DATE_CREATE, Date INCLUSION_DATE, Date EXCLUSION_DATE, int STATUS) {
        this.MANAGER_AGENCY_ID = MANAGER_AGENCY_ID;
        this.AGENCY_ID = AGENCY_ID;
        this.MANAGER_ID = MANAGER_ID;
        this.USER_CREATE = USER_CREATE;
        this.DATE_CREATE = DATE_CREATE;
        this.INCLUSION_DATE = INCLUSION_DATE;
        this.EXCLUSION_DATE = EXCLUSION_DATE;
        this.STATUS = STATUS;
    }

    public Manager_AgencyJpaEntity(BigInteger AGENCY_ID, BigInteger MANAGER_ID, BigInteger USER_CREATE, Date DATE_CREATE, Date INCLUSION_DATE, Date EXCLUSION_DATE, int STATUS) {
        this.AGENCY_ID = AGENCY_ID;
        this.MANAGER_ID = MANAGER_ID;
        this.USER_CREATE = USER_CREATE;
        this.DATE_CREATE = DATE_CREATE;
        this.INCLUSION_DATE = INCLUSION_DATE;
        this.EXCLUSION_DATE = EXCLUSION_DATE;
        this.STATUS = STATUS;
    }

    public Long getMANAGER_AGENCY_ID() {
        return MANAGER_AGENCY_ID;
    }

    public void setMANAGER_AGENCY_ID(Long MANAGER_AGENCY_ID) {
        this.MANAGER_AGENCY_ID = MANAGER_AGENCY_ID;
    }

    public BigInteger getAGENCY_ID() {
        return AGENCY_ID;
    }

    public void setAGENCY_ID(BigInteger AGENCY_ID) {
        this.AGENCY_ID = AGENCY_ID;
    }

    public BigInteger getMANAGER_ID() {
        return MANAGER_ID;
    }

    public void setMANAGER_ID(BigInteger MANAGER_ID) {
        this.MANAGER_ID = MANAGER_ID;
    }

    public BigInteger getUSER_CREATE() {
        return USER_CREATE;
    }

    public void setUSER_CREATE(BigInteger USER_CREATE) {
        this.USER_CREATE = USER_CREATE;
    }

    public Date getDATE_CREATE() {
        return DATE_CREATE;
    }

    public void setDATE_CREATE(Date DATE_CREATE) {
        this.DATE_CREATE = DATE_CREATE;
    }

    public Date getINCLUSION_DATE() {
        return INCLUSION_DATE;
    }

    public void setINCLUSION_DATE(Date INCLUSION_DATE) {
        this.INCLUSION_DATE = INCLUSION_DATE;
    }

    public Date getEXCLUSION_DATE() {
        return EXCLUSION_DATE;
    }

    public void setEXCLUSION_DATE(Date EXCLUSION_DATE) {
        this.EXCLUSION_DATE = EXCLUSION_DATE;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }
}
