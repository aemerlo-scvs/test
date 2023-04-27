package com.scfg.core.domain.smvs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class SMVSRequestDTO {
    private String names;
    private String identificationNumber;
    private Integer requestStatusIdc;
    private LocalDateTime requestDate;
}
