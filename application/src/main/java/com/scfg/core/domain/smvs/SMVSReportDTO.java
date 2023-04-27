package com.scfg.core.domain.smvs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class SMVSReportDTO {
    private String names;
    private String identificationNumber;
    private String ext;
    private String nationalityDescription;
    private String genderDescription;
    private int age;
    private String martialStatusDescription;
    private LocalDate birthDate;
    private String directionHome;
    private String telephoneHome;
    private String telephone;
    private String email;
    private String activateDescription;
    private LocalDateTime paymentDate;
    private String branchOffice;
    private String zones;
    private String agencyName;
    private String selleName;
    private String voucherNumber;
    private Double totalAmount;
    private String requestStatus;
    private String numberPolicy;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double insuredCapital;
    private Double totalPremium;
    private Double netPremium;
    private Double intermediaryCommission;
    private String policyStatusDescription;
    private Integer counts;
}
