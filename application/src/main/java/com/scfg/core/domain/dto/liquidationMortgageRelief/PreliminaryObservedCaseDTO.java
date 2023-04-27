package com.scfg.core.domain.dto.liquidationMortgageRelief;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
public class PreliminaryObservedCaseDTO {

    public PreliminaryObservedCaseDTO() {
    }

    @Builder
    public PreliminaryObservedCaseDTO(Long NUMERO_OPERACION, String NOMBRE_COMPLETO_ASEGURADO, String CEDULA_ASEGURADO, Double MONTO_ACUMULADO,
                                      String COMENTARIOS_MES_ACTUAL, Double DESEMBOLSO_MES_ACTUAL, Double DESEMBOLSO_MES_ANTERIOR,
                                      LocalDate FECHA_DESEMBOLSO) {
        this.NUMERO_OPERACION = NUMERO_OPERACION;
        this.NOMBRE_COMPLETO_ASEGURADO = NOMBRE_COMPLETO_ASEGURADO;
        this.CEDULA_ASEGURADO = CEDULA_ASEGURADO;
        this.MONTO_ACUMULADO = MONTO_ACUMULADO;
        this.COMENTARIOS_MES_ACTUAL = COMENTARIOS_MES_ACTUAL;
        this.DESEMBOLSO_MES_ACTUAL = DESEMBOLSO_MES_ACTUAL;
        this.DESEMBOLSO_MES_ANTERIOR = DESEMBOLSO_MES_ANTERIOR;
        this.FECHA_DESEMBOLSO = FECHA_DESEMBOLSO;
    }

    private Long NUMERO_OPERACION;

    private String NOMBRE_COMPLETO_ASEGURADO;

    private String CEDULA_ASEGURADO;

    private Double MONTO_ACUMULADO;

    private String COMENTARIOS_MES_ACTUAL;

    private Double DESEMBOLSO_MES_ACTUAL;

    private Double DESEMBOLSO_MES_ANTERIOR;

    private LocalDate FECHA_DESEMBOLSO;

}
