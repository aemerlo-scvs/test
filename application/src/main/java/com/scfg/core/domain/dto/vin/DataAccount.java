package com.scfg.core.domain.dto.vin;

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
public class DataAccount {
    @ApiModelProperty(notes = "Tipo de cuenta", example = "1", value = "1", required = true)
    private Integer accountType;
    @ApiModelProperty(notes = "Nro. Cuenta", example = "1", value = "12345678", required = true)
    private String accountNumber;
    @ApiModelProperty(notes = "Tipo moneda cuenta", example = "1", value = "2", required = true)
    private Integer accountCurrency;
}
