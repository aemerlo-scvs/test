package com.scfg.core.domain.dto.credicasas.groupthefont;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Null;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class MonthlyDisbursementGELDTO {
    private String NUMERO_OPERACION;
    private String FECHA_DESEMBOLSO;
    private String SEGURO;
    private String NUMERO_SOLICITUD;
    private String ASEGURADO;
    private String SEXO;
    private String NUMERO_IDENTIFICACION;
    private String EX;
    private String FECHA_NACIMIENTO;
    private String EDAD;
    private String NACIONALIDAD;
    private String PESO;
    private String ESTATURA;
    private String ESTADO;
    private String PLAZO;
    private String ESTADO_CONTRATO;
    private String MONEDA;
    private String SALDO_CONTRATO;
    private String TASA_MENSUAL;
    private String PRIMA_MENSUAL;
    private String OBSERVACIONES;
}
