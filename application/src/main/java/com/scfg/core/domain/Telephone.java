package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Telephone extends BaseDomain {
    @ApiModelProperty(notes = "Person (viene de: Person) ", example = "1")
    private Long personId;
    @ApiModelProperty(notes = "NewPerson (viene de: NewPerson)", example = "1")
    private Long newPersonId;
    @ApiModelProperty(notes = "Número de teléfono", example = "76336529")
    private String number;
    @ApiModelProperty(notes = "Tipo de teléfono (viene de: Classifier)", example = "1")
    private Integer telephoneTypeIdc;
    @ApiModelProperty(notes = "Número interno", example = "8238a")
    private String internalNumber;

    public Telephone() {}

    public Telephone(Long personId, String number, Integer telephoneTypeIdc) {
        this.personId = personId;
        this.number = number;
        this.telephoneTypeIdc = telephoneTypeIdc;
    }
}
