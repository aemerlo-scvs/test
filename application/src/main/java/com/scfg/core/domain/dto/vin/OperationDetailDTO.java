package com.scfg.core.domain.dto.vin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationDetailDTO {
    private String nro_operacion;
    private Integer tipo_documento;
    private String nro_documento;
    private String extension;
    private String complemento;
    private Long nro_comprobante;
    private Long cod_usuario;
    private String nombre_usuario;
    private Long cod_oficina;
    private String oficina;
}
