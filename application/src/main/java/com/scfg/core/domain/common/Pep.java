package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Pep extends BaseDomain {
    private String name;
    private String lastName;
    private String motherLastName;
    private String identificationNumber;
    private String issuancePlace;
    private String charge;
    private String countryCharge;
    private String pepType;

    public Pep(Long id, Date createdAt, Date lastModifiedAt, Long createdBy, Long lastModifiedBy, String name, String lastName, String motherLastName,
               String identificationNumber, String issuancePlace, String charge, String countryCharge, String pepType) {
        super(id, createdAt, lastModifiedAt, createdBy, lastModifiedBy);
        this.name = name;
        this.lastName = lastName;
        this.motherLastName = motherLastName;
        this.identificationNumber = identificationNumber;
        this.issuancePlace = issuancePlace;
        this.charge = charge;
        this.countryCharge = countryCharge;
        this.pepType = pepType;
    }

    @Override
    public String toString() {
        return "Pep{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", motherLastName='" + motherLastName + '\'' +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", issuancePlace='" + issuancePlace + '\'' +
                ", charge='" + charge + '\'' +
                ", countryCharge='" + countryCharge + '\'' +
                ", pepType='" + pepType + '\'' +
                '}';
    }
}
