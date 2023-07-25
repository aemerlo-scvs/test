package com.scfg.core.domain.person;

import com.scfg.core.domain.common.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model para crear una persona de referencia")
public class ReferencePerson extends BaseDomain {
    @ApiModelProperty(notes = "Persona (viene de: NewPerson)", example = "1")
    private Long personId;

    @ApiModelProperty(notes = "Nombre de persona de referencia", example = "reference Juan Perez")
    private String referencePersonName;

    @ApiModelProperty(notes = "Parentesco (viene de: Classifier)", example = "1")
    private Integer referenceRelationshipIdc;

    @ApiModelProperty(notes = "Teléfono de la persona de referencia (viene de: Classifier)", example = "78484848")
    private String referenceTelephone;

    @ApiModelProperty(notes = "Asegurado (boolean)", example = "1")
    private Integer referenceActivityIdc;
}
