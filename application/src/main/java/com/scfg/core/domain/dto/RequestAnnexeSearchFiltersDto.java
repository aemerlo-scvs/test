package com.scfg.core.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RequestAnnexeSearchFiltersDto {
    private String identificationNumber;
    private Integer requestStatusIdc;
    private String names;
    private String lastname;
    private String motherLastname;
    private Date fromDate;
    private Date toDate;
}
