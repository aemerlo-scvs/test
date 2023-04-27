package com.scfg.core.domain.dto.liquidationMortgageRelief;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class InsuredSummaryDTO {

    @JsonProperty("ID_ASEGURADO")
    private BigInteger ID_ASEGURADO;

    @JsonProperty("NOMBRE_COMPLETO")
    private String NOMBRE_COMPLETO;

    @JsonProperty("NRO_DOCUMENTO")
    private String NRO_DOCUMENTO;

    @JsonProperty("TIPO_DOCUMENTO")
    private String TIPO_DOCUMENTO;

    @JsonProperty("NUEVO_DESEMBOLSO")
    private String NUEVO_DESEMBOLSO;

    @JsonProperty("FECHA_ULTIMO_DESEMBOLSO")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date FECHA_ULTIMO_DESEMBOLSO;

    @JsonProperty("MONTO_DESEMBOLSADO")
    private Double MONTO_DESEMBOLSADO;

    @JsonProperty("MONTO_ACUMULADO")
    private Double MONTO_ACUMULADO;

    /*public InsuredSummaryDTO(){}

    @Builder
    public InsuredSummaryDTO(BigInteger ID_ASEGURADO, String NOMBRE_COMPLETO, String NRO_DOCUMENTO, String TIPO_DOCUMENTO, String NUEVO_DESEMBOLSO, Date FECHA_ULTIMO_DESEMBOLSO, Double MONTO_DESEMBOLSADO, Double MONTO_ACUMULADO) {
        this.ID_ASEGURADO = ID_ASEGURADO;
        this.NOMBRE_COMPLETO = NOMBRE_COMPLETO;
        this.NRO_DOCUMENTO = NRO_DOCUMENTO;
        this.TIPO_DOCUMENTO = TIPO_DOCUMENTO;
        this.NUEVO_DESEMBOLSO = NUEVO_DESEMBOLSO;
        this.FECHA_ULTIMO_DESEMBOLSO = FECHA_ULTIMO_DESEMBOLSO;
        this.MONTO_DESEMBOLSADO = MONTO_DESEMBOLSADO;
        this.MONTO_ACUMULADO = MONTO_ACUMULADO;
    }*/

    public InsuredSummaryDTO(Object[] objects){
        this.ID_ASEGURADO = (BigInteger) objects[0];
        this.NOMBRE_COMPLETO = (String) objects[1];
        this.NRO_DOCUMENTO = (String) objects[2];
        this.TIPO_DOCUMENTO = (String) objects[3];
        this.NUEVO_DESEMBOLSO = (String) objects[4];
        this.FECHA_ULTIMO_DESEMBOLSO = (Date) objects[5];
        this.MONTO_DESEMBOLSADO = (Double) objects[6];
        this.MONTO_ACUMULADO = (Double) objects[7];
    }

}
