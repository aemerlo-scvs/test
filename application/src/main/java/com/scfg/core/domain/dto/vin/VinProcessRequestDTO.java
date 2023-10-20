package com.scfg.core.domain.dto.vin;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.dto.CoverageDTO;
import com.scfg.core.domain.person.Person;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ApiModel(description = "DTO para realizar la solicitud")
public class VinProcessRequestDTO {
    @ApiModelProperty(notes = "Código Cliente", example = "123456", value = "123456", required = true)
    private Integer userCode;
    @ApiModelProperty(notes = "Nombre Cliente", example = "Fernando Flores", value = "Fernando Flores", required = true)
    private String userName;
    @ApiModelProperty(notes = "Correo Cliente", example = "123456", value = "123456", required = true)
    private String userEmail;
    @ApiModelProperty(notes = "Código Oficina", example = "123456", value = "123456", required = true)
    private Integer officeCode;
    @ApiModelProperty(notes = "Nombre Oficina", example = "123456", value = "123456", required = true)
    private String officeName;
    @ApiModelProperty(notes = "Nro. Operación", example = "123456", value = "1234565421", required = true)
    private String operationNumber;
    @ApiModelProperty(notes = "Código Producto", example = "123456", value = "532", required = true)
    private Integer productCode;
    @ApiModelProperty(notes = "Código Plan", example = "123456", value = "25", required = true)
    private Integer planCode;
    @ApiModelProperty(notes = "Tipo de Credito", example = "123456", value = "1", required = true)
    private Long creditType;
    @ApiModelProperty(notes = "Vigencia Credito", example = "123456", value = "60", required = true)
    private Integer creditTerm;
    @ApiModelProperty(notes = "Persona", example = "123456", value = "123456", required = true)
    private Person person;
    @ApiModelProperty(notes = "Nro. Telefono", example = "123456", value = "12345678", required = true)
    private String selectedCellphoneNumber;
    @ApiModelProperty(notes = "Listado de Nro. Telefono", required = true)
    private List<String> cellphoneNumbers;
    @ApiModelProperty(notes = "Nro. Cuenta", example = "123456", value = "123456", required = true)
    private DataAccount selectedAccount;
    @ApiModelProperty(notes = "Beneficiarios", example = "", value = "", required = true)
    private List<Beneficiary> beneficiaries;
    @ApiModelProperty(notes = "Coberturas", example = "", value = "", required = true)
    private List<CoverageDTO> coverages;
    @ApiModelProperty(notes = "Vigencia en años", example = "1", value = "1", required = true)
    private Integer termInYears;
    @ApiModelProperty(notes = "Vigencia en días", example = "123", value = "123", required = true)
    private Integer termInDays;
    @ApiModelProperty(notes = "Prima total", example = "123456", value = "123456", required = true)
    private Double totalPremium;
    @ApiModelProperty(notes = "Medio de envio", example = "", value = "", required = true)
    private Integer sendBy;
    @ApiModelProperty(notes = "Compañia Aseguradora", example = "", value = "", required = true)
    private Person insurerCompany;
    @ApiModelProperty(notes = "Compañia Tomadora", example = "", value = "", required = true)
    private Person holderCompany;
    @ApiModelProperty(notes = "Tipo de moneda", example = "", value = "", required = true)
    private Integer currencyTypeIdc;
}
