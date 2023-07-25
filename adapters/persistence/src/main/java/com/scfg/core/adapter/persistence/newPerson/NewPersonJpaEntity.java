package com.scfg.core.adapter.persistence.newPerson;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "NewPerson")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NewPersonJpaEntity extends BaseJpaEntity {

    @Column(name = "documentTypeIdc")
    private Integer documentTypeIdc;

    @Column(name = "identificationNumber", length = 15)
    private String identificationNumber;

    @Column(name = "extIdc")
    private Integer extIdc;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "lastName", length = 100)
    private String lastName;
    @Column(name = "motherLastName", length = 100)
    private String motherLastName;
    @Column(name = "marriedLastName", length = 100)
    private String marriedLastName;
    @Column(name = "genderIdc")
    private Integer genderIdc;
    @Column(name = "maritalStatusIdc")
    private Integer maritalStatusIdc;
    @Column(name = "birthDate")
    private LocalDateTime birthDate;
    @Column(name = "birthPlaceIdc")
    private Integer birthPlaceIdc;
    @Column(name = "nationalityIdc")
    private Integer nationalityIdc;

    @Column(name = "residencePlaceIdc")
    private Integer residencePlaceIdc;

    @Column(name = "activityIdc")
    private Integer activityIdc;
    @Column(name = "professionIdc", length = 200)
    private Integer professionIdc;
    @Column(name = "workerTypeIdc", length = 200)
    private Integer workerTypeIdc;

    @Column(name = "workerCompany", length = 200)
    private String workerCompany;

    @Column(name = "workEntryYear")
    private String workEntryYear;

    @Column(name = "workPosition", length = 200)
    private String workPosition;

    @Column(name = "monthlyIncomeRangeIdc", length = 200)
    private Integer monthlyIncomeRangeIdc;
    @Column(name = "yearlyIncomeRangeIdc", length = 200)
    private Integer yearlyIncomeRangeIdc;
    @Column(name = "businessTypeIdc", length = 200)
    private Integer businessTypeIdc;

    @Column(name = "businessRegistrationNumber", length = 200)
    private String businessRegistrationNumber;
    @Column(name = "email", length = 150)
    private String email;
    @Column(name = "eventualClient")
    private Integer eventualClient;
    @Column(name = "internalClientCode")
    private String internalClientCode;
    @Column(name = "institutionalClientCode")
    private String institutionalClientCode;

    @Column(name = "assignedGroupIdc")
    private Integer assignedGroupIdc;

}
