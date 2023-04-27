package com.scfg.core.domain.dto.credicasas.groupthefont;

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
public class GELPolicyDTO {

    Long id;

    String name;

    Long planId;
}
