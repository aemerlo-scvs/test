package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.config.ManualCertificateDhlDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonSerialize(using = ManualCertificateDhlDtoSerialize.class)
public class ManualCertificateDhlDTO {

    @JsonProperty("NRO_DJS_MANUAL")
    private String NRO_DJS_MANUAL;

    @JsonProperty("TIPO_POLIZA")
    private String TIPO_POLIZA;

    @JsonProperty("FECHA_LLENADO_DJS")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_LLENADO_DJS;

    @JsonProperty("NRO_OPERACION")
    private Long NRO_OPERACION;

    @JsonProperty("NOMBRE")
    private String NOMBRE;

    @JsonProperty("CI")
    private String CI;

    @JsonProperty("FECHA_NACIMIENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_NACIMIENTO;

    @JsonProperty("GENERO")
    private String GENERO;

    @JsonProperty("NACIONALIDAD")
    private String NACIONALIDAD;

    @JsonProperty("PESO")
    private Double PESO;

    @JsonProperty("ESTATURA")
    private Double ESTATURA;

    @JsonProperty("GESTOR")
    private String GESTOR;

    @JsonProperty("AGENCIA")
    private String AGENCIA;

    @JsonProperty("MONEDA")
    private String MONEDA;

    @JsonProperty("MONTO_SOLICITADO_BS")
    private Double MONTO_SOLICITADO_BS;

    @JsonProperty("MONTO_ACUMULADO_BS")
    private Double MONTO_ACUMULADO_BS;

    @JsonProperty("PLAZO_CREDITO")
    private Integer PLAZO_CREDITO;

    @JsonProperty("DIAS_MESES_ANHOS")
    private Integer DIAS_MESES_ANHOS;

    @JsonProperty("TIPO_CREDITO")
    private String TIPO_CREDITO;

    @JsonProperty("FECHA_ACEPTACION")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_ACEPTACION;

    @JsonProperty("NRO_CERTIFICADO")
    private String NRO_CERTIFICADO;

    @JsonProperty("ESTADO_SOLICITUD")
    private String ESTADO_SOLICITUD;


    @JsonProperty("FECHA_DESEMBOLSO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_DESEMBOLSO;

    @JsonProperty("VALOR_ASEGURADO")
    private Double VALOR_ASEGURADO;

    @JsonProperty("TASAX")
    private Double TASAX;

    @JsonProperty("COBERTURA")
    private String COBERTURA;

    @JsonProperty("TASA_EXTRAPRIMA")
    private Double TASA_EXTRAPRIMA;

    @JsonProperty("PRIMA_BS")
    private Double PRIMA_BS;

    @JsonProperty("TASA_EXTRAPRIMA_BANCO")
    private Double TASA_EXTRAPRIMA_BANCO;

    @JsonProperty("EXTRAPRIMA_BS")
    private Double EXTRAPRIMA_BS;

    // for relationship
    @Builder.Default
    private Long ID_SOLICITUD_SEGURO = null;

    @Builder.Default
    private Long ID_CLIENTE = null;

    @Builder.Default
    private Long ID_AGENCIA = null;

    @Builder.Default
    private Long ID_GESTOR = null;

    @Builder.Default
    private Long ID_MONEDA = null;

    @Builder.Default
    private Long ID_COBERTURA = null;

    @Builder.Default
    private Long ID_TIPO_CREDITO = null;

    @Builder.Default
    private Long ID_ITEM_DESGRAVAMEN = null;


    // for create
    @Builder.Default
    private MortgageReliefItem ITEM_DESGRAVAMEN = null;


    // for JSON API
    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    public Boolean HIDDEN_RELATIONSHIP = false;

    public ManualCertificateDhlDTO(){}

    @Builder
    public ManualCertificateDhlDTO(String NRO_DJS_MANUAL, String TIPO_POLIZA, LocalDate FECHA_LLENADO_DJS, Long NRO_OPERACION, String NOMBRE, String CI, LocalDate FECHA_NACIMIENTO, String GENERO, String NACIONALIDAD, Double PESO, Double ESTATURA, String GESTOR, String AGENCIA, String MONEDA, Double MONTO_SOLICITADO_BS, Double MONTO_ACUMULADO_BS, Integer PLAZO_CREDITO, Integer DIAS_MESES_ANHOS, String TIPO_CREDITO, LocalDate FECHA_ACEPTACION, String NRO_CERTIFICADO, String ESTADO_SOLICITUD, LocalDate FECHA_DESEMBOLSO, Double VALOR_ASEGURADO, Double TASAX, String COBERTURA, Double TASA_EXTRAPRIMA, Double PRIMA_BS, Double TASA_EXTRAPRIMA_BANCO, Double EXTRAPRIMA_BS) {
        this.NRO_DJS_MANUAL = NRO_DJS_MANUAL;
        this.TIPO_POLIZA = TIPO_POLIZA;
        this.FECHA_LLENADO_DJS = FECHA_LLENADO_DJS;
        this.NRO_OPERACION = NRO_OPERACION;
        this.NOMBRE = NOMBRE;
        this.CI = CI;
        this.FECHA_NACIMIENTO = FECHA_NACIMIENTO;
        this.GENERO = GENERO;
        this.NACIONALIDAD = NACIONALIDAD;
        this.PESO = PESO;
        this.ESTATURA = ESTATURA;
        this.GESTOR = GESTOR;
        this.AGENCIA = AGENCIA;
        this.MONEDA = MONEDA;
        this.MONTO_SOLICITADO_BS = MONTO_SOLICITADO_BS;
        this.MONTO_ACUMULADO_BS = MONTO_ACUMULADO_BS;
        this.PLAZO_CREDITO = PLAZO_CREDITO;
        this.DIAS_MESES_ANHOS = DIAS_MESES_ANHOS;
        this.TIPO_CREDITO = TIPO_CREDITO;
        this.FECHA_ACEPTACION = FECHA_ACEPTACION;
        this.NRO_CERTIFICADO = NRO_CERTIFICADO;
        this.ESTADO_SOLICITUD = ESTADO_SOLICITUD;
        this.FECHA_DESEMBOLSO = FECHA_DESEMBOLSO;
        this.VALOR_ASEGURADO = VALOR_ASEGURADO;
        this.TASAX = TASAX;
        this.COBERTURA = COBERTURA;
        this.TASA_EXTRAPRIMA = TASA_EXTRAPRIMA;
        this.PRIMA_BS = PRIMA_BS;
        this.TASA_EXTRAPRIMA_BANCO = TASA_EXTRAPRIMA_BANCO;
        this.EXTRAPRIMA_BS = EXTRAPRIMA_BS;
    }

    public ManualCertificateDhlDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }
}
