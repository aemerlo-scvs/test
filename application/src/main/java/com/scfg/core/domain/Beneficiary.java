package com.scfg.core.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Null;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Beneficiary extends BaseDomain {
    private String name;
    private String lastName;
    private String motherLastName;
    private String marriedLastName;
    private String identification;
    private LocalDateTime birthDate;
    private Integer percentage;
    private Integer relationshipIdc;
    private Long policyId;
    private Long policyItemId;
    private String cellphoneNumber;
    @Null
    private Integer isUnderAge;
    @Null
    private String representativeLegalName;
    @Null
    private String legalIdentification;
    @Null
    private Integer legalExt;
    @Null
    private String legalTelephone;

    public String getFullName(){
        String completeName = "";
        if (this.name != null) completeName += this.name.trim();
        if (this.lastName != null) completeName += " " + this.lastName.trim();
        if (this.motherLastName != null) completeName += " " + this.motherLastName.trim();
        if (this.marriedLastName != null) completeName += " " + this.marriedLastName.trim();
        return completeName;
    }
}
