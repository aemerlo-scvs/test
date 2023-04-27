package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.config.ConsolidatedObservedCaseDhlDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@JsonSerialize(using = ConsolidatedObservedCaseDhlDtoSerialize.class)
public class ConsolidatedObservedCaseDhlDTO implements Serializable {

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

    @JsonProperty("ACUMULADO")
    private Double ACUMULADO;

    @JsonProperty("COMENTARIOS_MES_ACTUAL")
    private String COMENTARIOS_MES_ACTUAL;

    @JsonProperty("CAPITAL_BS")
    private Double CAPITAL_BS;

    @JsonProperty("FECHA")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA;

    @JsonProperty("CONDICION")
    private String CONDICION;

    @JsonProperty("NRO_SOL_WEB")
    private String NRO_SOL_WEB;

    @JsonProperty("ESTADO_SOLICITUD")
    private String ESTADO_SOLICITUD;

    @JsonProperty("COMENTARIO")
    private String COMENTARIO;

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

    public ConsolidatedObservedCaseDhlDTO(){}

    @Builder
    public ConsolidatedObservedCaseDhlDTO(Long NRO_OPERACION, LocalDate FECHA_DESEMBOLSO, String CLIENTE, String CI, Double DESEMBOLSOS_ANTERIORES, Double DESEMBOLSOS_MES_ACTUAL, Double ACUMULADO, String COMENTARIOS_MES_ACTUAL, Double CAPITAL_USD, LocalDate FECHA, String CONDICION, String NRO_SOL_WEB, String ESTADO_SOLICITUD,String COMENTARIO, String ESTADO) {
        this.NRO_OPERACION = NRO_OPERACION;
        this.FECHA_DESEMBOLSO = FECHA_DESEMBOLSO;
        this.CLIENTE = CLIENTE;
        this.CI = CI;
        this.DESEMBOLSOS_ANTERIORES = DESEMBOLSOS_ANTERIORES;
        this.DESEMBOLSOS_MES_ACTUAL = DESEMBOLSOS_MES_ACTUAL;
        this.ACUMULADO = ACUMULADO;
        this.COMENTARIOS_MES_ACTUAL = COMENTARIOS_MES_ACTUAL;
        this.CAPITAL_BS = CAPITAL_USD;
        this.FECHA = FECHA;
        this.CONDICION = CONDICION;
        this.NRO_SOL_WEB = NRO_SOL_WEB;
        this.ESTADO_SOLICITUD = ESTADO_SOLICITUD;
        this.COMENTARIO = COMENTARIO;
        this.ESTADO = ESTADO;
    }

    public ConsolidatedObservedCaseDhlDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }
}
