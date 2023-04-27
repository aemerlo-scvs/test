package com.scfg.core.domain.dto.fabolous;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class FabolousReportDuplicateLiquidationDTO {
    private Long nro_cuenta;

    private String zona;

    private String sucursal;

    private String agencia;

    private Long cod_cliente;

    private String nombre_cliente;

    private String identificacion;

    private String ext;

    private String nacionalidad;

//    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha_nacimiento;

    private Double edad;

    private String estado_civil;

    private String domicilio;

    private String profesion;

    private String beneficiario;

    private Double porcentaje;

    private String parentesco;

    private String gestor;

//    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha_inicio_vigencia;

//    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha_fin_vigencia;

    private Double capital_asegurado_bs;

    private Double prima;

    private int mes;

    private String duplicados;

    private String exclusion;
}
