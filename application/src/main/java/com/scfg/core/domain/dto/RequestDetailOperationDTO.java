package com.scfg.core.domain.dto;

import com.scfg.core.domain.dto.vin.OperationDetailDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDetailOperationDTO {

    private String nro_operacion;
    private Integer tipo_documento;
    private String nro_documento;
    private Integer extensionIdc;
    private String complemento;

    public RequestDetailOperationDTO() {
    }

    public RequestDetailOperationDTO(OperationDetailDTO operationDetailDTO) {
        this.nro_operacion = operationDetailDTO.getNro_operacion();
        this.tipo_documento = operationDetailDTO.getTipo_documento();
        this.nro_documento = operationDetailDTO.getNro_documento();
        this.extensionIdc = 0;
        this.complemento = operationDetailDTO.getComplemento();
    }
}
