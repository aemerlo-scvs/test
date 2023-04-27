package com.scfg.core.domain.common;

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
@ApiModel(description = "Model para crear un historico de las solicitudes")
public class GeneralRequestAudit extends BaseDomain{

    @ApiModelProperty(notes = "Estado anterior", example = "1 (Pendiente)")
    private Integer oldStatusIdc;

    @ApiModelProperty(notes = "Estado nuevo", example = "2 (Activo)")
    private Integer newStatusIdc;

    @ApiModelProperty(notes = "Id solicitud", example = "1")
    private Long generalRequestId;

}
