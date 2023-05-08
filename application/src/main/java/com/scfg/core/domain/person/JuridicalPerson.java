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

    private Long internalClientCode;

    public JuridicalPerson (JuridicalPersonDomain juridicalPersonDomain) {
        this.setId(juridicalPersonDomain.getId());
        this.setName(juridicalPersonDomain.getName());
        this.setBusinessTypeIdc(juridicalPersonDomain.getBusinessTypeIdc());
        this.setWebSite(juridicalPersonDomain.getWebSite());
        this.setCreatedAt(juridicalPersonDomain.getCreatedAt());
        this.setLastModifiedAt(juridicalPersonDomain.getLastModifiedAt());
        this.setCreatedBy(juridicalPersonDomain.getCreatedBy());
        this.setLastModifiedBy(juridicalPersonDomain.getLastModifiedBy());
        this.setInternalClientCode(juridicalPersonDomain.getInternalClientCode());
    }
}
