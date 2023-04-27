package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.config.OldMonthlyDisbursementDhnDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonSerialize(using = OldMonthlyDisbursementDhnDtoSerialize.class)
public class OldMonthlyDisbursementCreditLineDhnDTO {

    @JsonProperty("USUARIO")
    private String USUARIO;

    @JsonProperty("NRO_POLIZA")
    private String NRO_POLIZA;

    @JsonProperty("CONTRATANTE")
    private String CONTRATANTE;

    @JsonProperty("CARTERA")
    private String CARTERA;

    @JsonProperty("NOMBRE_COMPLETO")
    private String NOMBRE_COMPLETO;

    @JsonProperty("CI")
    private String CI;

    @JsonProperty("FECHA_NACIMIENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_NACIMIENTO;

    @JsonProperty("NRO_SOLICITUD")
    private String NRO_SOLICITUD; // cambio

    @JsonProperty("FECHA_SOLICITUD")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_SOLICITUD;

    @JsonProperty("ESTADO")
    private String ESTADO;

    @JsonProperty("ACEPTACION")
    private String ACEPTACION;

    @JsonProperty("PORCENTAJE_EXTRAPRIMA")
    private Double PORCENTAJE_EXTRAPRIMA;

    @JsonProperty("MONEDA")
    private String MONEDA;

    @JsonProperty("MONTO_SOLICITADO")
    private Double MONTO_SOLICITADO;

    @JsonProperty("MONTO_ACUMULADO")
    private Double MONTO_ACUMULADO;

    @JsonProperty("FECHA_POSICION_COMPANIA")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_POSICION_COMPANIA;

    @JsonProperty("REGIONAL")
    private String REGIONAL;

    @JsonProperty("AGENCIA")
    private String AGENCIA;

    @JsonProperty("ADJUNTO_SOLICITUD")
    private String ADJUNTO_SOLICITUD;

    @JsonProperty("FECHA_DJS")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_DJS;

    @JsonProperty("ADJUNTO_CERTIFICADO")
    private String ADJUNTO_CERTIFICADO;

    @JsonProperty("FECHA_CERTIFICADO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_CERTIFICADO;

    // for relationship (MODIFIED)
    @Builder.Default
    private Long ID_REGIONAL = null;
    @Builder.Default
    private Long ID_CLIENTE = null;
    @Builder.Default
    private Long ID_AGENCIA = null;
    @Builder.Default
    private Long ID_SOLICITUD_SEGURO = null;
    @Builder.Default
    private Long ID_MONEDA = null;


    // for create
    @Builder.Default
    private MortgageReliefItem ITEM_DESGRAVAMEN = null;

    // for JSON API
    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    public Boolean HIDDEN_RELATIONSHIP = false;

    public OldMonthlyDisbursementCreditLineDhnDTO(){}

    @Builder
    public OldMonthlyDisbursementCreditLineDhnDTO(String USUARIO, String NRO_POLIZA, String CONTRATANTE, String CARTERA, String NOMBRE_COMPLETO, String CI, LocalDate FECHA_NACIMIENTO, String NRO_SOLICITUD, LocalDate FECHA_SOLICITUD, String ESTADO, String ACEPTACION, Double PORCENTAJE_EXTRAPRIMA, String MONEDA, Double MONTO_SOLICITADO, Double MONTO_ACUMULADO, LocalDate FECHA_POSICION_COMPANIA, String REGIONAL, String AGENCIA, String ADJUNTO_SOLICITUD, LocalDate FECHA_DJS, String ADJUNTO_CERTIFICADO, LocalDate FECHA_CERTIFICADO) {
        this.USUARIO = USUARIO;
        this.NRO_POLIZA = NRO_POLIZA;
        this.CONTRATANTE = CONTRATANTE;
        this.CARTERA = CARTERA;
        this.NOMBRE_COMPLETO = NOMBRE_COMPLETO;
        this.CI = CI;
        this.FECHA_NACIMIENTO = FECHA_NACIMIENTO;
        this.NRO_SOLICITUD = NRO_SOLICITUD;
        this.FECHA_SOLICITUD = FECHA_SOLICITUD;
        this.ESTADO = ESTADO;
        this.ACEPTACION = ACEPTACION;
        this.PORCENTAJE_EXTRAPRIMA = PORCENTAJE_EXTRAPRIMA;
        this.MONEDA = MONEDA;
        this.MONTO_SOLICITADO = MONTO_SOLICITADO;
        this.MONTO_ACUMULADO = MONTO_ACUMULADO;
        this.FECHA_POSICION_COMPANIA = FECHA_POSICION_COMPANIA;
        this.REGIONAL = REGIONAL;
        this.AGENCIA = AGENCIA;
        this.ADJUNTO_SOLICITUD = ADJUNTO_SOLICITUD;
        this.FECHA_DJS = FECHA_DJS;
        this.ADJUNTO_CERTIFICADO = ADJUNTO_CERTIFICADO;
        this.FECHA_CERTIFICADO = FECHA_CERTIFICADO;
    }

    public OldMonthlyDisbursementCreditLineDhnDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }
}
