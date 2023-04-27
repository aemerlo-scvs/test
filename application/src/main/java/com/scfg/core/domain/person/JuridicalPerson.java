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
public class JuridicalPerson extends BaseDomain {

    private String name;

    private Integer businessTypeIdc;

    private String webSite;
}
