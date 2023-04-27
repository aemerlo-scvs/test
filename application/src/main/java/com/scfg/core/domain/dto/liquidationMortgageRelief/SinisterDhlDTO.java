package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.config.ManualCertificateDhlDtoSerialize;
import com.scfg.core.domain.config.SinisterDhlDtoSerialize;
import com.scfg.core.domain.config.SinisterDhnDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@JsonSerialize(using = SinisterDhlDtoSerialize.class)
public class SinisterDhlDTO {


    @JsonProperty("NRO_SINIESTRO")
    private String NRO_SINIESTRO;

    @JsonProperty("CI_ASEGURADO")
    private String CI_ASEGURADO;

    @JsonProperty("ASEGURADO")
    private String ASEGURADO;

    @JsonProperty("ESTADO")
    private String ESTADO;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("FECHA_PAGO")
    private LocalDate FECHA_PAGO;

    // For relationship
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

    public SinisterDhlDTO(){}

    @Builder
    public SinisterDhlDTO(String NRO_SINIESTRO, String CI_ASEGURADO, String ASEGURADO, String ESTADO, LocalDate FECHA_PAGO) {
        this.NRO_SINIESTRO = NRO_SINIESTRO;
        this.CI_ASEGURADO = CI_ASEGURADO;
        this.ASEGURADO = ASEGURADO;
        this.ESTADO = ESTADO;
        this.FECHA_PAGO = FECHA_PAGO;
    }

    public SinisterDhlDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }
}
