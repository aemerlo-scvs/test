package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.config.ManualCertificateDhlDtoSerialize;
import com.scfg.core.domain.config.SinisterDhnDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@JsonSerialize(using = SinisterDhnDtoSerialize.class)
public class SinisterDhnDTO {


    @JsonProperty("NRO_SINIESTRO")
    private String NRO_SINIESTRO;

    @JsonProperty("CI_ASEGURADO")
    private String CI_ASEGURADO;

    @JsonProperty("ASEGURADO")
    private String ASEGURADO;

    @JsonProperty("ESTADO")
    private String ESTADO;

    @JsonProperty("FECHA_PAGO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_PAGO;

    // For relationship
    @Builder.Default
    private Long ID_TOMADOR = null;

    @Builder.Default
    private Long ID_CLIENTE = null;

    @Builder.Default
    private Long ID_ITEM_DESGRAVAMEN = null;

    // For create
    @Builder.Default
    private MortgageReliefItem ITEM_DESGRAVAMEN = null;

    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    public Boolean HIDDEN_RELATIONSHIP = false;

    public SinisterDhnDTO(){}


    @Builder
    public SinisterDhnDTO(String NRO_SINIESTRO, String CI_ASEGURADO, String ASEGURADO, String ESTADO, LocalDate FECHA_PAGO) {
        this.NRO_SINIESTRO = NRO_SINIESTRO;
        this.CI_ASEGURADO = CI_ASEGURADO;
        this.ASEGURADO = ASEGURADO;
        this.ESTADO = ESTADO;
        this.FECHA_PAGO = FECHA_PAGO;
    }

    public SinisterDhnDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }
}
