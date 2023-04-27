package com.scfg.core.domain.dto.credicasas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CommercialReportDTO {
    private String cartera;
    private String poliza;
    private Timestamp fechaLlenadoDjs;
    private String noOperacion;
    private String tipoOperacion;
    private Integer noSolicitud;
    private String noCertificado;
    private String asegurado;
    private String genero;
    private String ci;
    private String ext;
    private LocalDate fechaNac;
    private BigDecimal edad;
    private String nacionalidad;
    private BigDecimal peso;
    private BigDecimal estatura;
    private String tipoAsegurado;
    private String moneda;
    private Double montoVigente;
    private Double montoSolicitado;
    private Double montoAcumulado;
    private Integer plazaCredito;
    private String estado;
    private String motPendiente;
    private String motInactivacion;
    private String tipoAceptacion;
    private Double porcentExtPrim;
    private String motExtPrim;
    private String motExc;
    private String motRec;
    private String comentRec;
    private String cobOtorgada;
    private LocalDate fechaPronunciamiento;
    private LocalDate fechaCargaDjs;
    private LocalDate fechaCargaCert;
    private LocalDate fechaCargaNotRec;
    private String userSucur;
    private String userCreation;
    private LocalDate fechaOperacion;
    private String subsType;
    private String tipFirma;
}
