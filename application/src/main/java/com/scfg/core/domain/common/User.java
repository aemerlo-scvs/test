package com.scfg.core.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ApiModel(description = "Model para crear un nuevo usuario")
public class User extends BaseDomain {

    public User(Long id, Date createdAt, Date lastModifiedAt, Long createdBy, Long lastModifiedBy, String name, String surName, Long genderIdc, String email, String userName, String password, String secretQuestion, String secretAnswer, Long regionalIdc, Long officeIdc, Long companyIdc, String token, Long roleId) {
        super(id, createdAt, lastModifiedAt, createdBy, lastModifiedBy);
        this.name = name;
        this.surName = surName;
        this.genderIdc = genderIdc;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
        this.regionalIdc = regionalIdc;
        this.officeIdc = officeIdc;
        this.companyIdc = companyIdc;
        this.token = token;
        this.roleId = roleId;
    }

    @ApiModelProperty(notes = "Nombre", example = "Juan")
    private String name;

    @ApiModelProperty(notes = "Apellido", example = "Flores")
    private String surName;

    @ApiModelProperty(notes = "Género (viene de: Classifier)", example = "1 ")
    private Long genderIdc;

    @ApiModelProperty(notes = "Correo", example = "fjuan@santacruzfg.com")
    private String email;

    @ApiModelProperty(notes = "Nombre de usuario (por defecto es el nombre de su correo antes del @)", example = "fjuan")
    public String userName;

    @ApiModelProperty(notes = "Contraseña", example = "123 ")
    public String password;

    @ApiModelProperty(notes = "Pregunta Secreta", example = "...")
    private String secretQuestion;

    @ApiModelProperty(notes = "Respuesta Secreta", example = "...")
    private String secretAnswer;

    @ApiModelProperty(notes = "Regional (viene de: Classifier)", example = "8 ")
    private Long regionalIdc;

    @ApiModelProperty(notes = "Oficina (viene de: Classifier)", example = "1 ")
    private Long officeIdc;

    @ApiModelProperty(notes = "Empresa (viene de: Classifier)", example = "1 ")
    private Long companyIdc;

    @ApiModelProperty(notes = "Token (es null por defecto)", example = "null")
    private String token;

    @ApiModelProperty(notes = "Rol (viene de: Role)", example = "6 ")
    private Long roleId;

    @ApiModelProperty(notes = "Codigo de usuario del Banco", example = "1002215 ")
    private Long bfsUserCode;

    @ApiModelProperty(notes = "Codigo de agencia del Banco", example = "1002215 ")
    private Long bfsAgencyCode;

    public String getCompleteName() {
        String completeName = "";
        if (this.name != null) completeName += this.name.trim();
        if (this.surName != null) completeName += " " + this.surName.trim();
        return completeName;
    }
}
