package com.scfg.core.domain.person;
import com.scfg.core.domain.Telephone;
import com.scfg.core.domain.common.BaseDomain;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.dto.vin.Account;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ApiModel(description = "Model para crear una persona")

public class NewPerson extends BaseDomain {
    @ApiModelProperty(notes = "Tipo de documento (viene de: Classifier)", example = "1")
    private Integer documentTypeIdc;

    @ApiModelProperty(notes = "Número de identificación", example = "7895456123")
    private String identificationNumber;

    @ApiModelProperty(notes = "Extensión (viene de: Classifier)", example = "2")
    private Integer extIdc;

    @ApiModelProperty(notes = "Nombre", example = "Juan")
    private String name;

    @ApiModelProperty(notes = "Apellido", example = "Perez")
    private String lastName;

    @ApiModelProperty(notes = "Apellido materno", example = "Perez2")
    private String motherLastName;

    @ApiModelProperty(notes = "Apellido de casada", example = "Perez3")
    private String marriedLastName;

    @ApiModelProperty(notes = "Género (viene de: Classifier)", example = "1")
    private Integer genderIdc;

    @ApiModelProperty(notes = "Estado civil (viene de: Classifier)", example = "1")
    private Integer maritalStatusIdc;

    @ApiModelProperty(notes = "Fecha de nacimiento")
    private LocalDateTime birthDate;

    @ApiModelProperty(notes = "Lugar de nacimiento (viene de: Classifier)", example = "5")
    private Integer birthPlaceIdc;

    @ApiModelProperty(notes = "Nacionalidad (viene de: Classifier)", example = "6")
    private Integer nationalityIdc;

    @ApiModelProperty(notes = "Lugar de residencia (viene de: Classifier)", example = "6")
    private Integer residencePlaceIdc;

    @ApiModelProperty(notes = "Actividad/ocupación (viene de: Classifier) ", example = "2")
    private Integer activityIdc;

    @ApiModelProperty(notes = "Profesión (viene de: Classifier) ", example = "5")
    private Integer professionIdc;

    @ApiModelProperty(notes = "Tipo de trabajador (viene de: Classifier) ", example = "10")
    private Integer workerTypeIdc;

    @ApiModelProperty(notes = "Empresa donde trabaja", example = "SCVS")
    private String workerCompany;

    @ApiModelProperty(notes = "Año de ingreso a la empresa", example = "2023")
    private String workEntryYear;

    @ApiModelProperty(notes = "Puesto de trabajo", example = "cajero")
    private String workPosition;

    @ApiModelProperty(notes = "Rango de ingreso mensual (viene de: Classifier) ", example = "1")
    private Integer monthlyIncomeRangeIdc;

    @ApiModelProperty(notes = "Rango de ingreso anual (viene de: Classifier) ", example = "2")
    private Integer yearlyIncomeRangeIdc;

    @ApiModelProperty(notes = "Tipo de organización/negocio (viene de: Classifier)", example = "3")
    private Integer businessTypeIdc;

    @ApiModelProperty(notes = "Número de Matrícula de Comercio", example = "5451231551")
    private String businessRegistrationNumber;

    @ApiModelProperty(notes = "Correo electrónico", example = "algo@gmail.com.bo")
    private String email;

    @ApiModelProperty(notes = "Cliente eventual", example = "1")
    private Integer eventualClient;

    @ApiModelProperty(notes = "Código interno del cliente", example = "123456")
    private String internalClientCode;

    @ApiModelProperty(notes = "Código institucional del cliente", example = "7895123")
    private String institutionalClientCode;

    @ApiModelProperty(notes = "Grupo empresarial asignado (viene de: Classifier)", example = "1")
    private Integer assignedGroupIdc;

    @ApiModelProperty(notes = "Código cliente (algo referente al banco)", example = "12345789")
    private String clientCode;

    @ApiModelProperty(notes = "Tipo de cliente (Algo referente al banco)", example = "2")
    private Integer clientType;

    @ApiModelProperty(notes = "Id de persona antigua (viene de: Person)", example = "1")
    private Long personId;

    @ApiModelProperty(notes = "Estado", example = "1")
    private Integer status;
    private ReferencePerson spouse;
    private List<ReferencePerson> referencePersonInfo;
    private List<Telephone> telephones;
    private List<Direction> directions;
    private List<Account> accounts;
    private List<PersonRole> relatedPersons;

}
