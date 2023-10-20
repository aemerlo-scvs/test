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
@ApiModel(description = "Model para crear una persona de relacionada - Jurídicos")
public class PersonRole extends BaseDomain {
    @ApiModelProperty(notes = "Persona (viene de: NewPerson)", example = "1")
    private Long personId;

    @ApiModelProperty(notes = "Persona relacionada (viene de: NewPerson)", example = "4")
    private String relatedPersonId;

    @ApiModelProperty(notes = "Rol de la persona (viene de: Classifier)", example = "1")
    private Integer relationshipRolIdc;

}
