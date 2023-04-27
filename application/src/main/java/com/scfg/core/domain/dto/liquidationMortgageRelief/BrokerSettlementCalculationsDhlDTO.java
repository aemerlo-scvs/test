package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scfg.core.domain.config.BrokerSettlementCalculationsDhlDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@Getter
@Setter
@JsonSerialize(using = BrokerSettlementCalculationsDhlDtoSerialize.class)
public class BrokerSettlementCalculationsDhlDTO  {


    @JsonProperty("ITEMS")
    private String ITEMS;

    @JsonProperty("VALOR_ASEGURADO")
    private String VALOR_ASEGURADO;

    @JsonProperty("PRIMA")
    private Double PRIMA;

    @JsonProperty("COM_FASSIL")
    private Double COM_FASSIL;

    @JsonProperty("CANT_ASEGURADOS")
    private Integer CANT_ASEGURADOS;

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
    /*@FieldNameConstants.Exclude()
    @ToString.Exclude
    @EqualsAndHashCode.Exclude*/
    /*@Setter(value=AccessLevel.NONE)
    @Getter(value=AccessLevel.NONE)*/
    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    public Boolean HIDDEN_RELATIONSHIP = false;

    public BrokerSettlementCalculationsDhlDTO(){}

    @Builder
    public BrokerSettlementCalculationsDhlDTO(String ITEMS, String VALOR_ASEGURADO, Double PRIMA, Double COM_FASSIL, Integer CANT_ASEGURADOS) {
        this.ITEMS = ITEMS;
        this.VALOR_ASEGURADO = VALOR_ASEGURADO;
        this.PRIMA = PRIMA;
        this.COM_FASSIL = COM_FASSIL;
        this.CANT_ASEGURADOS = CANT_ASEGURADOS;
    }

    public BrokerSettlementCalculationsDhlDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }
}
