package com.scfg.core.domain.dto;

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
public class JuridicalPersonWithPersonIdDTO {
    private Long id;
    private String name;
    private Integer businessTypeIdc;
    private Long personId;
}
