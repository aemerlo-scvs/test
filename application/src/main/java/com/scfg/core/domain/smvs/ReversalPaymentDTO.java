package com.scfg.core.domain.smvs;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ApiModel(description = "DTO para revertir los pagos")
public class ReversalPaymentDTO {
    @ApiModelProperty(notes = "Nro Comprobante", example = "100", value = "100", required = true)
    private String nro_comprobante;

    @ApiModelProperty(notes = "Id Usuario", example = "124546", value = "124546", required = true)
    private Integer id_usuario;

    @ApiModelProperty(notes = "Nombre Usuario", example = "Juan Pérez", value = "Juan Pérez", required = true)
    private String nombre_usuario;

    @ApiModelProperty(notes = "Id Agencia", example = "102", value = "102", required = true)
    private Integer id_agencia;

    @ApiModelProperty(notes = "Nombre Agencia", example = "Aroma", value = "Aroma", required = true)
    private String nombre_agencia;

}
