package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.config.SubscriptionTrackingDhlDtoSerialize;
import com.scfg.core.domain.config.SubscriptionTrackingDhnDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonSerialize(using = SubscriptionTrackingDhnDtoSerialize.class)
public class SubscriptionTrackingDhnDTO {

    @JsonProperty("CORRELATIVO_CONTROL")
    private Long CORRELATIVO_CONTROL;

    @JsonProperty("ITEM")
    private String ITEM;

    @JsonProperty("FECHA_RECEPCION_DJS")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_RECEPCION_DJS;

    @JsonProperty("FECHA_LLENADO_DJS")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_LLENADO_DJS;

    @JsonProperty("NRO_DECLARACION")
    private String NRO_DECLARACION;

    @JsonProperty("NRO_OPERACION")
    private Long NRO_OPERACION;

    @JsonProperty("ASEGURADO")
    private String ASEGURADO;

    @JsonProperty("GENERO")
    private String GENERO;

    @JsonProperty("CI")
    private String CI;

    @JsonProperty("EXTENSION")
    private String EXTENSION;

    @JsonProperty("FECHA_NACIMIENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_NACIMIENTO;

    @JsonProperty("MONEDA")
    private String MONEDA;

    @JsonProperty("MONTO_SOLICITADO_BS")
    private Double MONTO_SOLICITADO_BS;

    @JsonProperty("MONTO_ACUMULADO_BS")
    private Double MONTO_ACUMULADO_BS;

    @JsonProperty("REQUISITOS")
    private String REQUISITOS; // split

    @JsonProperty("FECHA_CUMPLIMIENTO_REQUISITOS")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_CUMPLIMIENTO_REQUISITOS;

    @JsonProperty("ESTADO")
    private String ESTADO;

    @JsonProperty("CAUSA")
    private String CAUSA;

    @JsonProperty("COMENTARIOS_ADICIONALES")
    private String COMENTARIOS_ADICIONALES;

    @JsonProperty("FECHA_PRONUNCIAMIENTO_AL_BANCO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_PRONUNCIAMIENTO_AL_BANCO;

    @JsonProperty("FECHA_DESEMBOLSO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_DESEMBOLSO;

    @JsonProperty("TIEMPO_VALIDEZ_DJS")
    private Integer TIEMPO_VALIDEZ_DJS;

    @JsonProperty("TIEMPO_RESPUESTA")
    private Integer TIEMPO_RESPUESTA;

    @JsonProperty("CARTA_EXCLUSION")
    private String CARTA_EXCLUSION;

    @JsonProperty("RIESGO")
    private String RIESGO;

    @JsonProperty("TASA_EXTRAPRIMA")
    private Double TASA_EXTRAPRIMA;

    @JsonProperty("MOTIVO_EXTRAPRIMA")
    private String MOTIVO_EXTRAPRIMA;

    @JsonProperty("COBERTURA_OTORGADA")
    private String COBERTURA_OTORGADA;

    @JsonProperty("DETALLE_MOTIVO")
    private String DETALLE_MOTIVO;

    @JsonProperty("EXAMENES_REALIZADOS")
    private String EXAMENES_REALIZADOS;

    @JsonProperty("OFICINA")
    private String OFICINA;

    @JsonProperty("SUCURSAL_OFICINA")
    private String SUCURSAL_OFICINA;

    @JsonProperty("GESTOR")
    private String GESTOR;

    @JsonProperty("TIPO_REQUERIMIENTO")
    private String TIPO_REQUERIMIENTO;

    @JsonProperty("FECHA_ENVIO_REASEGURO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_ENVIO_REASEGURO;

    @JsonProperty("TIPO_OPERACION")
    private String TIPO_OPERACION;

    @JsonProperty("FECHA_RESPUESTA")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_RESPUESTA;

    @JsonProperty("TIEMPO_RESPUESTA_REASEGURO")
    private Integer TIEMPO_RESPUESTA_REASEGURO;

    @JsonProperty("CAPITAL_ASEGURADO_BS")
    private Double CAPITAL_ASEGURADO_BS;

    @JsonProperty("NIVEL")
    private String NIVEL;

    @JsonProperty("PLAZO_CREDITO_MESES")
    private Integer PLAZO_CREDITO_MESES;

    @JsonProperty("FECHA_EMISION")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_EMISION;

    @JsonProperty("COMENTARIOS")
    private String COMENTARIOS;

    @JsonProperty("PROCESO")
    private String PROCESO;

    @JsonProperty("FECHA_AGENDAMIENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_AGENDAMIENTO;


    // for relationship
    @Builder.Default
    private Long ID_REGIONAL = null;

    @Builder.Default
    private Long ID_CLIENTE = null;

    @Builder.Default
    private Long ID_GESTOR = null;

    @Builder.Default
    private Long ID_TIPO_COBERTURA = null;

    @Builder.Default
    private Long ID_AGENCIA = null;

    @Builder.Default
    private Long ID_SOLICITUD_SEGURO = null;

    @Builder.Default
    private Long ID_MONEDA = null;

    @Builder.Default
    private Long ID_ITEM_DESGRAVAMEN = null;

    // for create
    @Builder.Default
    private MortgageReliefItem ITEM_DESGRAVAMEN = null;

    // for JSON API
    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    public Boolean HIDDEN_RELATIONSHIP = false;


    public SubscriptionTrackingDhnDTO(){}

    @Builder
    public SubscriptionTrackingDhnDTO(Long CORRELATIVO_CONTROL, String ITEM, LocalDate FECHA_RECEPCION_DJS, LocalDate FECHA_LLENADO_DJS, String NRO_DECLARACION, Long NRO_OPERACION, String ASEGURADO, String GENERO, String CI, String EXTENSION, LocalDate FECHA_NACIMIENTO, String MONEDA, Double MONTO_SOLICITADO_BS, Double MONTO_ACUMULADO_BS, String REQUISITOS, LocalDate FECHA_CUMPLIMIENTO_REQUISITOS, String ESTADO, String CAUSA, String COMENTARIOS_ADICIONALES, LocalDate FECHA_PRONUNCIAMIENTO_AL_BANCO, LocalDate FECHA_DESEMBOLSO, Integer TIEMPO_VALIDEZ_DJS, Integer TIEMPO_RESPUESTA, String CARTA_EXCLUSION, String RIESGO, Double TASA_EXTRAPRIMA, String MOTIVO_EXTRAPRIMA, String COBERTURA_OTORGADA, String DETALLE_MOTIVO, String EXAMENES_REALIZADOS, String OFICINA, String SUCURSAL_OFICINA, String GESTOR, String TIPO_REQUERIMIENTO, LocalDate FECHA_ENVIO_REASEGURO, String TIPO_OPERACION, LocalDate FECHA_RESPUESTA, Integer TIEMPO_RESPUESTA_REASEGURO, Double CAPITAL_ASEGURADO_BS, String NIVEL, Integer PLAZO_CREDITO_MESES, LocalDate FECHA_EMISION, String COMENTARIOS, String PROCESO, String ID, LocalDate FECHA_AGENDAMIENTO) {
        this.CORRELATIVO_CONTROL = CORRELATIVO_CONTROL;
        this.ITEM = ITEM;
        this.FECHA_RECEPCION_DJS = FECHA_RECEPCION_DJS;
        this.FECHA_LLENADO_DJS = FECHA_LLENADO_DJS;
        this.NRO_DECLARACION = NRO_DECLARACION;
        this.NRO_OPERACION = NRO_OPERACION;
        this.ASEGURADO = ASEGURADO;
        this.GENERO = GENERO;
        this.CI = CI;
        this.EXTENSION = EXTENSION;
        this.FECHA_NACIMIENTO = FECHA_NACIMIENTO;
        this.MONEDA = MONEDA;
        this.MONTO_SOLICITADO_BS = MONTO_SOLICITADO_BS;
        this.MONTO_ACUMULADO_BS = MONTO_ACUMULADO_BS;
        this.REQUISITOS = REQUISITOS;
        this.FECHA_CUMPLIMIENTO_REQUISITOS = FECHA_CUMPLIMIENTO_REQUISITOS;
        this.ESTADO = ESTADO;
        this.CAUSA = CAUSA;
        this.COMENTARIOS_ADICIONALES = COMENTARIOS_ADICIONALES;
        this.FECHA_PRONUNCIAMIENTO_AL_BANCO = FECHA_PRONUNCIAMIENTO_AL_BANCO;
        this.FECHA_DESEMBOLSO = FECHA_DESEMBOLSO;
        this.TIEMPO_VALIDEZ_DJS = TIEMPO_VALIDEZ_DJS;
        this.TIEMPO_RESPUESTA = TIEMPO_RESPUESTA;
        this.CARTA_EXCLUSION = CARTA_EXCLUSION;
        this.RIESGO = RIESGO;
        this.TASA_EXTRAPRIMA = TASA_EXTRAPRIMA;
        this.MOTIVO_EXTRAPRIMA = MOTIVO_EXTRAPRIMA;
        this.COBERTURA_OTORGADA = COBERTURA_OTORGADA;
        this.DETALLE_MOTIVO = DETALLE_MOTIVO;
        this.EXAMENES_REALIZADOS = EXAMENES_REALIZADOS;
        this.OFICINA = OFICINA;
        this.SUCURSAL_OFICINA = SUCURSAL_OFICINA;
        this.GESTOR = GESTOR;
        this.TIPO_REQUERIMIENTO = TIPO_REQUERIMIENTO;
        this.FECHA_ENVIO_REASEGURO = FECHA_ENVIO_REASEGURO;
        this.TIPO_OPERACION = TIPO_OPERACION;
        this.FECHA_RESPUESTA = FECHA_RESPUESTA;
        this.TIEMPO_RESPUESTA_REASEGURO = TIEMPO_RESPUESTA_REASEGURO;
        this.CAPITAL_ASEGURADO_BS = CAPITAL_ASEGURADO_BS;
        this.NIVEL = NIVEL;
        this.PLAZO_CREDITO_MESES = PLAZO_CREDITO_MESES;
        this.FECHA_EMISION = FECHA_EMISION;
        this.COMENTARIOS = COMENTARIOS;
        this.PROCESO = PROCESO;
        this.FECHA_AGENDAMIENTO = FECHA_AGENDAMIENTO;
    }

    public SubscriptionTrackingDhnDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }

}
