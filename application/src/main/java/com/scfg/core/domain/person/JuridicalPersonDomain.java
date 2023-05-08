package com.scfg.core.domain.person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class JuridicalPersonDomain {
    private String name;

    private Integer businessTypeIdc;

    private Long nit;

    private String webSite;

    private PersonDomain person;

    private Long id;

    private Date createdAt;

    private Date lastModifiedAt;

    private Long createdBy;

    private Long lastModifiedBy;

    private Long internalClientCode;
}
