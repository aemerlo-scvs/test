package com.scfg.core.domain.dto;

import com.scfg.core.domain.common.BaseDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ApiModel(description = "Model para crear el registro de los mensajes que se envien por correo, sms o whatsapp")
public class MessageDTO extends BaseDomain {

    @ApiModelProperty(notes = "Tipo de Mensaje", example = "1", value = "1", allowableValues = "1, 2, 3", required = true)
    private Integer messageTypeIdc;

    @ApiModelProperty(notes = "Id de Referencia", example = "1", value = "1", required = true)
    @Column(name = "referenceId")
    private Long referenceId;

    @ApiModelProperty(notes = "Mensaje", example = "Hola", value = "Hola")
    @Column(name = "message", length = 500)
    private String message;

    @ApiModelProperty(notes = "Para", example = "Hola", value = "Hola")
    @Column(name = "to", length = 500)
    private String to;

    @ApiModelProperty(notes = "Copia para", example = "Hola", value = "Hola")
    @Column(name = "cc", length = 500)
    private String cc;

    @ApiModelProperty(notes = "Para []", example = "Hola", value = "Hola")
    @Column(name = "sendTo", length = 500)
    private String[] sendTo;

    @ApiModelProperty(notes = "Copia para []", example = "Hola", value = "Hola")
    @Column(name = "sendCc", length = 500)
    private String[] sendCc;

    @ApiModelProperty(notes = "Asunto", example = "Hola", value = "Hola")
    @Column(name = "subject", length = 500)
    private String subject;

    @ApiModelProperty(notes = "Tabla de referencia", example = "1", value = "1", allowableValues = "1, 2, 3", required = true)
    private Integer referenceTableIdc;

    @ApiModelProperty(notes = "Observación", example = "Error al enviar el mensaje", value = "Error al enviar el mensaje")
    @Column(name = "observation", length = 1000)
    private String observation;

    @ApiModelProperty(notes = "Número de intentos", example = "0", value = "0")
    @Column(name = "numberOfAttempt")
    private Integer numberOfAttempt;

    @ApiModelProperty(notes = "Número de intentos", example = "0", value = "0")
    @Column(name = "lastNumberOfAttempt")
    private Integer lastNumberOfAttempt;
}
