package com.scfg.core.domain.common;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

public class ReferencePerson extends BaseDomain{
    @ApiModelProperty(notes = "Persona (viene de: NewPerson)", example = "1")
    private Long personId;

    @ApiModelProperty(notes = "Nombre de persona de referencia", example = "Juan Perez")
    private String referencePersonName;

    @ApiModelProperty(notes = "Parentesco (viene de: Classifier)", example = "1")
    private Integer referenceRelationshipIdc;

    @ApiModelProperty(notes = "Teléfono de la persona de referencia (viene de: Classifier)", example = "78484848")
    private String referenceTelephone;

    @ApiModelProperty(notes = "Asegurado (boolean)", example = "1")
    private Integer referenceActivityIdc;
}
