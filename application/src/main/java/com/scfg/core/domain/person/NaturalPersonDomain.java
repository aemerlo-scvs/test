package com.scfg.core.domain.person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class NaturalPersonDomain {
    private Integer clientCode;

    private Integer clientEventual;

    private Integer clientType;

    private String name;

    private String lastName;

    private String motherLastName;

    private String marriedLastName;

    private Integer maritalStatusIdc;

    private Integer documentType;

    private String identificationNumber;

    private String complement;

    private Integer extIdc;

    private LocalDateTime birthDate;

    private Integer genderIdc;

    private String profession;

    private String workPlace;

    private Integer workTypeIdc;

    private String position;

    private LocalDateTime entryDate;

    private Double salary;

    private Long nit;

    private Long internalClientCode;

    private PersonDomain person;

    private Long id;

    private Date createdAt;

    private Date lastModifiedAt;

    private Long createdBy;

    private Long lastModifiedBy;

}
