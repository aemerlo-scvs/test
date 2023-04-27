package com.scfg.core.domain.report.sepelio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Reportbean {
    private String policyNumber;
    private String fullName;
    private String identificationNumber;
    private String identificationExtension;
    private String homeAddress;
    private String phoneNumber;
    private String occupation;
    private String gender;
    private String amountInsured;
    private String dateValidateFrom;
    private Double amountTotal;
    private String datePolicy;
    private String siteCity;
    private String fullNameRequest;
    private String firmDigital;
    private String lugar;
    private String dateRequest;
    private List<ReportBeneficiaries> beneficiary;
    private List<ReportLegals> legals;
}
