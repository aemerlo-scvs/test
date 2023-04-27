package com.scfg.core.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SearchRequestDTO {
    private Long productId;
    private Long planId;
    private Integer requestStatusIdc;
    private Integer requestNumber;
    private Date fromDate;
    private Date toDate;
    private String name;
    private String lastName;
    private String motherLastName;
    private String identificationNumber;
}
