package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class CommercialManagementSearchFiltersDTO {
    private Date fromDate;
    private Date toDate;
    private Integer status;
    private Integer subStatus;
    private String identificationNumber;
    private String number;
}
