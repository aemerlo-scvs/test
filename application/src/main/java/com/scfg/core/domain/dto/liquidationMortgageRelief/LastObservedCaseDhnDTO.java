package com.scfg.core.domain.dto.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfg.core.domain.common.Classifier;
import com.scfg.core.domain.common.Client;
import com.scfg.core.domain.common.CreditOperation;
import com.scfg.core.domain.config.LastObservedCaseDhlDtoSerialize;
import com.scfg.core.domain.config.LastObservedCaseDhnDtoSerialize;
import com.scfg.core.domain.liquidationMortgageRelief.MortgageReliefItem;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonSerialize(using = LastObservedCaseDhnDtoSerialize.class)
public class LastObservedCaseDhnDTO {

    // agregar este campo a la base de datos
    @JsonProperty("NRO")
    private Long NRO;

    @JsonProperty("NRO_OPERACION")
    private Long NRO_OPERACION;

    @JsonProperty("NRO_SOLICITUD")
    private String NRO_SOLICITUD;

    @JsonProperty("NOMBRE")
    private String NOMBRE;

    @JsonProperty("NRO_CI")
    private String NRO_CI;

    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("FECHA_NACIMIENTO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_NACIMIENTO;

    @JsonProperty("SALDO")
    private Double SALDO;

    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("FECHA_DESEMBOLSO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate FECHA_DESEMBOLSO;

    @JsonProperty("CAPITAL_EXCLUIDO")
    private Double CAPITAL_EXCLUIDO;

    @JsonProperty("TIPO_OBSERVACION")
    private String TIPO_OBSERVACION;

    // for relationship
    @Builder.Default
    private Long ID_TIPO_OBSERVACION = null;

    @Builder.Default
    private Long ID_CLIENTE = null;

    @Builder.Default
    private Long ID_OPERACION_CREDITICIA = null;

    @Builder.Default
    private Long ID_ITEM_DESGRAVAMEN = null;



    // for create
    @Builder.Default
    private MortgageReliefItem ITEM_DESGRAVAMEN = null;

    @Builder.Default
    //@JsonInclude(value = JsonInclude.Include.NON_NULL)
    public Boolean HIDDEN_RELATIONSHIP = false;

    public LastObservedCaseDhnDTO(){}

    @Builder
    public LastObservedCaseDhnDTO(Long NRO, Long NRO_OPERACION, String NRO_SOLICITUD, String NOMBRE, String NRO_CI, LocalDate FECHA_NACIMIENTO, Double SALDO, LocalDate FECHA_DESEMBOLSO, Double CAPITAL_EXCLUIDO, String TIPO_OBSERVACION) {
        this.NRO = NRO;
        this.NRO_OPERACION = NRO_OPERACION;
        this.NRO_SOLICITUD = NRO_SOLICITUD;
        this.NOMBRE = NOMBRE;
        this.NRO_CI = NRO_CI;
        this.FECHA_NACIMIENTO = FECHA_NACIMIENTO;
        this.SALDO = SALDO;
        this.FECHA_DESEMBOLSO = FECHA_DESEMBOLSO;
        this.CAPITAL_EXCLUIDO = CAPITAL_EXCLUIDO;
        this.TIPO_OBSERVACION = TIPO_OBSERVACION;
    }

    public LastObservedCaseDhnDTO hiddenRelationship(){
        HIDDEN_RELATIONSHIP = true;
        return this;
    }


}
