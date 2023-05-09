package com.scfg.core.domain.dto.vin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class GenerateDocSettlement {

    private String policyName;
    private String policyNumber;
    private Date policyFromDate;
    private Date policyToDate;
    private Integer crediTermInYears;
    private Date requestDate;
    private Integer daysPassed;
    private Integer yearsPassed;
    private String accountNumber;
    private Double amountAccepted;
    private String currencyAbbreviation;
    private String currencyDesc;
    private Double currencyDollarValue;
    private Double premiumPaid;
    private Double premiumPaidAnnual;
    private Double adminExpenses;
    private Double discountProrataDay;
    private Double valueToReturn;
    private String assuranceName;
    private String assuranceIdentificationNumber;
    private String assuranceIdentificationExtension;
    private String insurerCompanyName;
    private String cellphone;
    private String nroSettlement;
    private String city;
    private Double insuredCapital;

}
