package com.scfg.core.domain.dto.credicasas;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class FilterParameter {
    private Integer id;
    private Integer requestStatusIdc;
    private String name;
    private String lastName;
    private String motherLastName;
    private String identificationNumber;
    private String organizationName;
    private Date fromDate;
    private Date toDate;
}
