package com.scfg.core.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCancelOperationDTO {

    private String nro_operacion;

    private Long nro_comprobante;

    private String motivo;

    private Long cod_usuario;

    private String nombre_usuario;

}
