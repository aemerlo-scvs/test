package com.scfg.core.domain.dto.virh;

import com.scfg.core.domain.Beneficiary;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.AnswerDTO;
import com.scfg.core.domain.person.NewPerson;
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
public class VirhDTO {
    @ApiModelProperty(notes = "Plan de donde se origina", example = "1")
    private Long planId;
    @ApiModelProperty(notes = "Tamaño", example = "166")
    private Double height;
    @ApiModelProperty(notes = "Peso", example = "66")
    private Double weight;
    @ApiModelProperty(notes = "Codigo unico de referencia", example = "121-1s5-45a")
    private String uniqueCode;
    @ApiModelProperty(notes = "Cumulo total", example = "5000")
    private Double cumulus;
    @ApiModelProperty(notes = "Prima del plan", example = "5000")
    private Double planPremium;
    @ApiModelProperty(notes = "Capital asegurado del cliente acorde al monto seleccionado", example = "5000")
    private Double insuredAmount;
    private NewPerson person;
    private List<Beneficiary> beneficiaryList;
    private List<AnswerDTO> answers;
}
