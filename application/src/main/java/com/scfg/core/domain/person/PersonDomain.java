package com.scfg.core.domain.person;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PersonDomain extends BaseDomain {

    private Integer nationalityIdc;

    private Integer residenceIdc;

    private Integer activityIdc;

    private String reference;

    private String telephone;

    private String email;

    private Integer holder;

    private Integer insured;

    private Integer assignedGroupIdc;
}
