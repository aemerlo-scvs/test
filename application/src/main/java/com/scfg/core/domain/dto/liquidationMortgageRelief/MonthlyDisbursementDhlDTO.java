package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.config.ManualCertificateDhlDtoSerialize;
import com.scfg.core.domain.config.MonthlyDisbursementDhlDtoSerialize;
import com.scfg.core.domain.config.MonthlyDisbursementDhnDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@JsonSerialize(using = MonthlyDisbursementDhlDtoSerialize.class)
public class MonthlyDisbursementDhlDTO {

    @JsonProperty("NRO_OPERACION")
    private Long NRO_OPERACION;

    @JsonProperty("NOMBRES")
    private String NOMBRES;

    @JsonProperty("APELLIDO_PATERNO")
    private String APELLIDO_PATERNO;

    @JsonProperty("APELLIDO_MATERNO")
    private String APELLIDO_MATERNO;

    @JsonProperty("APELLIDO_CASADA")
    private String APELLIDO_CASADA;

    @JsonProperty("TIPO_DOCUMENTO")
    private String TIPO_DOCUMENTO;

    @JsonProperty("NRO_DOCUMENTO")
    private String NRO_DOCUMENTO;

    @JsonProperty("COPIA_DUPLICADO")
    private String COPIA_DUPLICADO;

    @JsonProperty("EXTENSION")
    private String EXTENSION;

    @JsonProperty("PLAZA")
    private String PLAZA;

    @JsonProperty("FECHA_DESEMBOLSO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_DESEMBOLSO;

    @JsonProperty("VALOR_ASEGURADO")
    private Double VALOR_ASEGURADO;

    @JsonProperty("TASAX")
    private Double TASAX;

    @JsonProperty("FECHA_NACIMIENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_NACIMIENTO;

    @JsonProperty("MONTO_DESEMBOLSADO")
    private Double MONTO_DESEMBOLSADO;

    @JsonProperty("FECHA_VENCIMIENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_VENCIMIENTO;

    @JsonProperty("MONEDA")
    private String MONEDA;

    @JsonProperty("TIPO_CREDITO")
    private String TIPO_CREDITO;

    @JsonProperty("ASEGURADO")
    private String ASEGURADO;

    @JsonProperty("COBERTURA")
    private String COBERTURA;

    @JsonProperty("SEXO")
    private String SEXO;

    @JsonProperty("PERIODO")
    private String PERIODO;

    @JsonProperty("LINEA_CREDITO")
    private String LINEA_CREDITO;

    @JsonProperty("PLAZO_CREDITO_DIAS")
    private Integer PLAZO_CREDITO_DIAS;

    @JsonProperty("EXTRAPRIMA")
    private Double EXTRAPRIMA;

    @JsonProperty("NACIONALIDAD")
    private String NACIONALIDAD;

    @JsonProperty("AGENCIA")
    private String AGENCIA;

    @JsonProperty("MONTO_PRIMA")
    private Double MONTO_PRIMA;

    @JsonProperty("PAGADO_DESDE")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate PAGADO_DESDE;

    @JsonProperty("PAGADO_HASTA")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate PAGADO_HASTA;

//    private Integer MONTH_ORDER;
//
//    private Integer YEAR_ORDER;

    // for relationship

    @Builder.Default
    private Long ID_CLIENTE = null;

    @Builder.Default
    private Long ID_OPERACION_CREDITICIA = null;

    @Builder.Default
    private Long ID_AGENCIA = null;

    @Builder.Default
    private Long ID_TIPO_CREDITO = null;

    @Builder.Default
    private Long ID_TIPO_COBERTURA = null;

    @Builder.Default
    private Long ID_MONEDA = null;

    @Builder.Default
    private Long ID_ITEM_DESGRAVAMEN = null;

    // For Observed Case
    @Builder.Default
    private Long CASE_IN_ORDEN = null;



    // for create
    @Builder.Default
    private MortgageReliefItem ITEM_DESGRAVAMEN = null;

    // for JSON API
    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    public Boolean HIDDEN_RELATIONSHIP = false;

    public MonthlyDisbursementDhlDTO(){}

    @Builder
    public MonthlyDisbursementDhlDTO(Long NRO_OPERACION, String NOMBRES, String APELLIDO_PATERNO, String APELLIDO_MATERNO, String APELLIDO_CASADA, String TIPO_DOCUMENTO, String NRO_DOCUMENTO, String COPIA_DUPLICADO, String EXTENSION, String PLAZA, LocalDate FECHA_DESEMBOLSO, Double VALOR_ASEGURADO, Double TASAX, LocalDate FECHA_NACIMIENTO, Double MONTO_DESEMBOLSADO, LocalDate FECHA_VENCIMIENTO, String MONEDA, String TIPO_CREDITO, String ASEGURADO, String COBERTURA, String SEXO, String PERIODO, String LINEA_CREDITO, Integer PLAZO_CREDITO_DIAS, Double EXTRAPRIMA, String NACIONALIDAD, String AGENCIA, Double MONTO_PRIMA, LocalDate PAGADO_DESDE, LocalDate PAGADO_HASTA, Integer MONTH_ORDER, Integer YEAR_ORDER) {
        this.NRO_OPERACION = NRO_OPERACION;
        this.NOMBRES = NOMBRES;
        this.APELLIDO_PATERNO = APELLIDO_PATERNO;
        this.APELLIDO_MATERNO = APELLIDO_MATERNO;
        this.APELLIDO_CASADA = APELLIDO_CASADA;
        this.TIPO_DOCUMENTO = TIPO_DOCUMENTO;
        this.NRO_DOCUMENTO = NRO_DOCUMENTO;
        this.COPIA_DUPLICADO = COPIA_DUPLICADO;
        this.EXTENSION = EXTENSION;
        this.PLAZA = PLAZA;
        this.FECHA_DESEMBOLSO = FECHA_DESEMBOLSO;
        this.VALOR_ASEGURADO = VALOR_ASEGURADO;
        this.TASAX = TASAX;
        this.FECHA_NACIMIENTO = FECHA_NACIMIENTO;
        this.MONTO_DESEMBOLSADO = MONTO_DESEMBOLSADO;
        this.FECHA_VENCIMIENTO = FECHA_VENCIMIENTO;
        this.MONEDA = MONEDA;
        this.TIPO_CREDITO = TIPO_CREDITO;
        this.ASEGURADO = ASEGURADO;
        this.COBERTURA = COBERTURA;
        this.SEXO = SEXO;
        this.PERIODO = PERIODO;
        this.LINEA_CREDITO = LINEA_CREDITO;
        this.PLAZO_CREDITO_DIAS = PLAZO_CREDITO_DIAS;
        this.EXTRAPRIMA = EXTRAPRIMA;
        this.NACIONALIDAD = NACIONALIDAD;
        this.AGENCIA = AGENCIA;
        this.MONTO_PRIMA = MONTO_PRIMA;
        this.PAGADO_DESDE = PAGADO_DESDE;
        this.PAGADO_HASTA = PAGADO_HASTA;
//        this.MONTH_ORDER = MONTH_ORDER;
//        this.YEAR_ORDER = YEAR_ORDER;
    }



    public MonthlyDisbursementDhlDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyDisbursementDhlDTO that = (MonthlyDisbursementDhlDTO) o;
        return Objects.equals(NRO_OPERACION, that.NRO_OPERACION) && Objects.equals(NRO_DOCUMENTO, that.NRO_DOCUMENTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NRO_OPERACION, NRO_DOCUMENTO);
    }
}
