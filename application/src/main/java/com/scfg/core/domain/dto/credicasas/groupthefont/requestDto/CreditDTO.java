package com.scfg.core.domain.dto.credicasas.groupthefont.requestDto;

import com.scfg.core.domain.dto.FileDocumentDTO;
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
public class CreditDTO {
    private Long insuredTypeIdc;
    private String creditNumber;
    private Integer creditTerm;
    private Double validAmount;
    private Double requestedAmount;
    private Double accumulatedAmount;
}
