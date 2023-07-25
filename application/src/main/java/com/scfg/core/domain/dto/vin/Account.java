package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.common.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Account extends BaseDomain {

    @ApiModelProperty(notes = "Tipo de cuenta (viene de: Classifier)", example = "1")
    private Integer accountTypeIdc;
    @ApiModelProperty(notes = "Número de cuenta", example = "10000056456548613")
    private String accountNumber;
    @ApiModelProperty(notes = "Tipo de moneda (viene de: Classifier)", example = "1")
    private Integer accountCurrencyTypeIdc;
    @ApiModelProperty(notes = "Person (viene de: Person)", example = "1")
    private Long personId;
    @ApiModelProperty(notes = "Entidad financiera (viene de: Classifier)", example = "1")
    private Integer financialEntityIdc;
    @ApiModelProperty(notes = "Nombre del propietario", example = "Juan Perez")
    private String accountPersonName;
    @ApiModelProperty(notes = "Nro de identificación del propietario", example = "25456461")
    private Long accountIdentificationNumber;
    @ApiModelProperty(notes = "NewPerson (viene de: NewPerson)", example = "1")
    private Long newPersonId;

}
