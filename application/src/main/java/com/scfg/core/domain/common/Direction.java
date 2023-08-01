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

    @ApiModelProperty(notes = "Persona (viene de: Person)", example = "1")
    private Long personId;
    @ApiModelProperty(notes = "Persona (viene de: NewPerson)", example = "1")
    private Long newPersonId;
    @ApiModelProperty(notes = "Departamento (viene de: Classifier)", example = "1")
    private Integer departmentIdc;
    @ApiModelProperty(notes = "Ciudad (viene de: Classifier)", example = "1")
    private Integer cityIdc;
    @ApiModelProperty(notes = "Referencia de la dirección", example = "Al frente de una ventita")
    private String referenceDirection;
}
