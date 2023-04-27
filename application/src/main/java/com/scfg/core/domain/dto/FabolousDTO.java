package com.scfg.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class FabolousDTO implements Serializable {

    private Long nro_cuenta;

    private String zona;

    private String sucursal;

    private String agencia;

    private Long cod_cliente;

    private String nombre_cliente;

    private String identificacion;

    private String nacionalidad;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha_nacimiento;

    private String estado_civil;

    private String domicilio;

    private String profesion;

    private String beneficiario;

    private double porcentaje;

    private String parentesco;

    private String gestor;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha_inicio_vigencia;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha_fin_vigencia;

    private double capital_asegurado_bs;

    //Section for others parameters to others tables

    private Long user;

    private Long reportType;

    private Long policy;

    private Long month;

    private Long year;

}
