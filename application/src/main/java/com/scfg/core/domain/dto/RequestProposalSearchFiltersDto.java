package com.scfg.core.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestProposalSearchFiltersDto {
    private Long userId;
    private String identificationNumber;
    private Integer requestStatusIdc;
    private String names;
    private String lastname;
    private String motherLastname;
    private String operationNumber;
    private String fromDate;
    private String toDate;

    private boolean initialSearch;
}
