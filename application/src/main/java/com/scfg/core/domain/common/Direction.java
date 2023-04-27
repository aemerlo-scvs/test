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
@ApiModel(description = "Model para crear una dirección")
public class Direction extends BaseDomain {

    @ApiModelProperty(notes = "Descripción", example = "C/ los pinos")
    private String description;

    @ApiModelProperty(notes = "Tipo de Dirección (viene de: Classifier)", example = "1")
    private Integer directionTypeIdc;

    @ApiModelProperty(notes = "Número de Celular", example = "72182046")
    private String cellPhone;

    @ApiModelProperty(notes = "Persona (viene de: Person)", example = "1")
    private Long personId;
}
