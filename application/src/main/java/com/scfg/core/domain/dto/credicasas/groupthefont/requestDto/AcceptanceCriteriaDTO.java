package com.scfg.core.domain.dto.credicasas.groupthefont.requestDto;

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
public class AcceptanceCriteriaDTO {
    private Double imc;
    private Boolean isPEP;
    private Double weight;
    private Double height;
    private Boolean wasRejected;
}
