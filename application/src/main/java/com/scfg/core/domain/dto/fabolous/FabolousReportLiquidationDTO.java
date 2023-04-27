package com.scfg.core.domain.dto.fabolous;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class FabolousReportLiquidationDTO implements Serializable {

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

    @Builder
    public FabolousReportLiquidationDTO(Long nro_cuenta, String zona, String sucursal, String agencia, Long cod_cliente, String nombre_cliente, String identificacion, String ext, String nacionalidad, LocalDate fecha_nacimiento, Double edad, String estado_civil, String domicilio, String profesion, String beneficiario, Double porcentaje, String parentesco, String gestor, LocalDate fecha_inicio_vigencia, LocalDate fecha_fin_vigencia, Double capital_asegurado_bs, Double prima) {
        this.nro_cuenta = nro_cuenta;
        this.zona = zona;
        this.sucursal = sucursal;
        this.agencia = agencia;
        this.cod_cliente = cod_cliente;
        this.nombre_cliente = nombre_cliente;
        this.identificacion = identificacion;
        this.ext = ext;
        this.nacionalidad = nacionalidad;
        this.fecha_nacimiento = fecha_nacimiento;
        this.edad = edad;
        this.estado_civil = estado_civil;
        this.domicilio = domicilio;
        this.profesion = profesion;
        this.beneficiario = beneficiario;
        this.porcentaje = porcentaje;
        this.parentesco = parentesco;
        this.gestor = gestor;
        this.fecha_inicio_vigencia = fecha_inicio_vigencia;
        this.fecha_fin_vigencia = fecha_fin_vigencia;
        this.capital_asegurado_bs = capital_asegurado_bs;
        this.prima = prima;
    }

    public FabolousReportLiquidationDTO() {
    }
}
