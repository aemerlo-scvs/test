package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.config.ConsolidatedObservedCaseDhnDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@JsonSerialize(using = ConsolidatedObservedCaseDhnDtoSerialize.class)
public class ConsolidatedObservedCaseDhnDTO implements Serializable {

    @JsonProperty("NRO_OPERACION")
    private Long NRO_OPERACION;

    @JsonProperty("FECHA_DESEMBOLSO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_DESEMBOLSO;

    @JsonProperty("CLIENTE")
    private String CLIENTE;

    @JsonProperty("CI")
    private String CI;

    @JsonProperty("DESEMBOLSOS_ANTERIORES")
    private Double DESEMBOLSOS_ANTERIORES;

    @JsonProperty("DESEMBOLSOS_MES_ACTUAL")
    private Double DESEMBOLSOS_MES_ACTUAL;

    @JsonProperty("MONTO_ACUMULADO")
    private Double MONTO_ACUMULADO;

    @JsonProperty("COMENTARIOS_MES_ACTUAL")
    private String COMENTARIOS_MES_ACTUAL;

    @JsonProperty("CAPITAL_BS")
    private Double CAPITAL_BS;

    @JsonProperty("FECHA")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA;

    @JsonProperty("CONDICION")
    private String CONDICION;

    @JsonProperty("NRO_SOLICITUD_WEB")
    private String NRO_SOLICITUD_WEB;

    @JsonProperty("ESTADO_SOLICITUD")
    private String ESTADO_SOLICITUD;

    @JsonProperty("COMENTARIOS_BROKER")
    private String COMENTARIOS_BROKER;

    @JsonProperty("ESTADO")
    private String ESTADO;

    // For relationships
    @Builder.Default
    private Long ID_CASO_OBSERVADO = null;

    @Builder.Default
    private Long ID_SOLICITUD = null;

    @Builder.Default
    private Long ID_TIPO_MONEDA = null;

    @Builder.Default
    private Long ID_ITEM_DESGRAVAMEN = null;

    // For create
    @Builder.Default
    private MortgageReliefItem ITEM_DESGRAVAMEN = null;

    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    public Boolean HIDDEN_RELATIONSHIP = false;

    public ConsolidatedObservedCaseDhnDTO(){}

    @Builder
    public ConsolidatedObservedCaseDhnDTO(Long NRO_OPERACION, LocalDate FECHA_DESEMBOLSO, String CLIENTE, String CI, Double DESEMBOLSOS_ANTERIORES, Double DESEMBOLSOS_MES_ACTUAL, Double MONTO_ACUMULADO, String COMENTARIOS_MES_ACTUAL, Double CAPITAL_BS, LocalDate FECHA, String CONDICION, String NRO_SOLICITUD_WEB, String ESTADO_SOLICITUD, String COMENTARIOS_BROKER, String ESTADO) {
        this.NRO_OPERACION = NRO_OPERACION;
        this.FECHA_DESEMBOLSO = FECHA_DESEMBOLSO;
        this.CLIENTE = CLIENTE;
        this.CI = CI;
        this.DESEMBOLSOS_ANTERIORES = DESEMBOLSOS_ANTERIORES;
        this.DESEMBOLSOS_MES_ACTUAL = DESEMBOLSOS_MES_ACTUAL;
        this.MONTO_ACUMULADO = MONTO_ACUMULADO;
        this.COMENTARIOS_MES_ACTUAL = COMENTARIOS_MES_ACTUAL;
        this.CAPITAL_BS = CAPITAL_BS;
        this.FECHA = FECHA;
        this.CONDICION = CONDICION;
        this.NRO_SOLICITUD_WEB = NRO_SOLICITUD_WEB;
        this.ESTADO_SOLICITUD = ESTADO_SOLICITUD;
        this.COMENTARIOS_BROKER = COMENTARIOS_BROKER;
        this.ESTADO = ESTADO;
    }


    public ConsolidatedObservedCaseDhnDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }
}
