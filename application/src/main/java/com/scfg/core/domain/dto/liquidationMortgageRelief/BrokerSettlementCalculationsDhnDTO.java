package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.config.BrokerSettlementCalculationsDhnDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.IOException;
@Getter
@Setter
@JsonSerialize(using = BrokerSettlementCalculationsDhnDtoSerialize.class)
public class BrokerSettlementCalculationsDhnDTO  {

    @JsonProperty("LINEA")
    private String LINEA;

    @JsonProperty("CAPITAL_ASEGURADO")
    private String CAPITAL_ASEGURADO;

    @JsonProperty("PRIMA")
    private Double PRIMA;

    @JsonProperty("EXTRAPRIMA")
    private Double EXTRAPRIMA;

    @JsonProperty("TOTAL_PRIMA")
    private Double TOTAL_PRIMA;

    @JsonProperty("PRIMA_COMPANIA")
    private Double PRIMA_COMPANIA;

    @JsonProperty("SERVICIO_COBRANZA")
    private Double SERVICIO_COBRANZA;

    @JsonProperty("TOTAL_ASEGURADOS")
    private Integer TOTAL_ASEGURADOS;

    // For relationship
    @Builder.Default
    private Long ID_COBERTURA_ASEGURADOS = null;

    @Builder.Default
    private Long ID_ITEM_DESGRAVAMEN = null;

    // For create
    @Builder.Default
    private MortgageReliefItem ITEM_DESGRAVAMEN = null;


    // For JSON API
    //@JsonIgnore
    /*@Getter(value= AccessLevel.NONE)
    @Setter(value=AccessLevel.NONE)*/
    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Boolean HIDDEN_RELATIONSHIP = false;

    /*@FieldNameConstants.Exclude()
    @ToString.Exclude
    @EqualsAndHashCode.Exclude*/

    public BrokerSettlementCalculationsDhnDTO(){ }

    @Builder
    public BrokerSettlementCalculationsDhnDTO(String LINEA, String CAPITAL_ASEGURADO, Double PRIMA, Double EXTRAPRIMA, Double TOTAL_PRIMA, Double PRIMA_COMPANIA, Double SERVICIO_COBRANZA, Integer TOTAL_ASEGURADOS) {
        this.LINEA = LINEA;
        this.CAPITAL_ASEGURADO = CAPITAL_ASEGURADO;
        this.PRIMA = PRIMA;
        this.EXTRAPRIMA = EXTRAPRIMA;
        this.TOTAL_PRIMA = TOTAL_PRIMA;
        this.PRIMA_COMPANIA = PRIMA_COMPANIA;
        this.SERVICIO_COBRANZA = SERVICIO_COBRANZA;
        this.TOTAL_ASEGURADOS = TOTAL_ASEGURADOS;
    }

    public BrokerSettlementCalculationsDhnDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }
}
