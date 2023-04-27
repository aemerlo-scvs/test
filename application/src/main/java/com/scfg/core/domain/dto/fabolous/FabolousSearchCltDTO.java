package com.scfg.core.domain.dto.fabolous;

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
public class FabolousSearchCltDTO {
    private String name;
    private String documentNumber;
}
