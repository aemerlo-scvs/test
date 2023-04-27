package com.scfg.core.domain.dto;

import java.io.Serializable;
import java.util.Date;

public class ReportRequestPolicyDTO implements Serializable {
    private static final long serialVersionUID = -1025200354799942264L;
    private int codsucursal;
    private int codzona;
    private int codagencia;
    private int codgestor;
    private Date datefrom;
    private Date dateto;

    public int getCodsucursal() {
        return codsucursal;
    }

    public void setCodsucursal(int codsucursal) {
        this.codsucursal = codsucursal;
    }

    public int getCodzona() {
        return codzona;
    }

    public void setCodzona(int codzona) {
        this.codzona = codzona;
    }

    public int getCodagencia() {
        return codagencia;
    }

    public void setCodagencia(int codagencia) {
        this.codagencia = codagencia;
    }

    public int getCodgestor() {
        return codgestor;
    }

    public void setCodgestor(int codgestor) {
        this.codgestor = codgestor;
    }

    public Date getDatefrom() {
        return datefrom;
    }

    public void setDatefrom(Date datefrom) {
        this.datefrom = datefrom;
    }

    public Date getDateto() {
        return dateto;
    }

    public void setDateto(Date dateto) {
        this.dateto = dateto;
    }
}
