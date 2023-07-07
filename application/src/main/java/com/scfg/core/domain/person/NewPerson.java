package com.scfg.core.domain.person;
import com.scfg.core.domain.Telephone;
import com.scfg.core.domain.common.BaseDomain;
import com.scfg.core.domain.common.Direction;
import com.scfg.core.domain.dto.vin.Account;

import java.time.LocalDateTime;
import java.util.List;

public class NewPerson extends BaseDomain {
    private Integer documentTypeIdc;
    private String identificationNumber;
    private Integer extIdc;
    private String name;
    private String lastName;
    private String motherLastName;
    private String marriedLastName;
    private Integer genderIdc;
    private Integer maritalStatusIdc;
    private LocalDateTime birthDate;
    private Integer birthPlaceIdc;
    private Integer nationalityIdc;
    private Integer residencePlaceIdc;
    private Integer activityIdc;
    private String professionIdc;
    private Integer workerTypeIdc;
    private String workerCompany;
    private String workEntryYear;
    private String WorkPosition;
    private Integer monthlyIncomeRangeIdc;
    private Integer yearlyIncomeRangeIdc;
    private Integer businessTypeIdc;
    private String businessRegistrationNumber;
    private String email;
    private Integer eventualClient;
    private Integer internalClientCode;
    private Integer institutionalClientCode;
    private List<Telephone> telephones;
    private List<Direction> directions;
    private List<Account> accounts;

}
