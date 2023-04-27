package com.scfg.core.domain.dto.credicasas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ClfPlanDTO {

    private Long productId;
    private Long planId;
    private String planName;
}
