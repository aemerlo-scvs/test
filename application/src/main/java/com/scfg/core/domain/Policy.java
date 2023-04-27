package com.scfg.core.domain;

import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersMethods;
import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Policy extends BaseDomain {

    private Long generalRequestId;
    private Integer currencyTypeIdc;
    private Integer exchangeRate;
    private Long correlativeNumber;
    private String numberPolicy;
    private Date issuanceDate;
    private Date fromDate;
    private Date toDate;
    private Double totalPremium;
    private Double additionalPremium;
    private Double netPremium;
    private Double riskPremium;
    private Double aps;

    private Double fpa;
    private Double iva;
    private Double it;
    private Double intermediaryCommission;
    private Double intermediaryCommissionPercentage;
    private Double collectionServiceCommission;//fk recursion
    private Double collectionServiceCommissionPercentage;
    private Double insuredCapital;
    private Integer policyStatusIdc;
    private Long branchOfficeId;
    private Long agencyId;
    private Integer renewal;
    private Long assignedExecutiveId;
    private Integer intermediaryTypeIdc;
    private Long brokerId;
    private Long agentId;

    private Long productId;

    //#region Custom Constructor

    // SMVS Constructor
    public Policy(Long generalRequestId, Integer currencyTypeIdc, Long numberPolicyNext, String numberPolicy, Double premium, Double insuredCapital,
                  Integer policyStatusIdc, Long productId) {

        Date validateFrom = DateUtils.asDate(LocalDate.now());
        Date issuanceDate = new Date();
        Date validated = DateUtils.asSummaryRestartDays(validateFrom, Calendar.YEAR, 1);

        this.generalRequestId = generalRequestId;
        this.currencyTypeIdc = currencyTypeIdc;
        this.exchangeRate = 0;
        this.correlativeNumber = numberPolicyNext;
        this.numberPolicy = numberPolicy;
        this.issuanceDate = issuanceDate;
        this.fromDate = DateUtils.asLocalDateTimeStamp(DateUtils.asDateToLocalDate(validateFrom));
        this.toDate = DateUtils.asLocalDateTimeStamp(DateUtils.asDateToLocalDate(validated));
        this.totalPremium = premium;
        this.additionalPremium = 0.0;
        this.netPremium = premium;
        this.riskPremium = 0.0;
        this.aps = 0.0;
        this.iva = 0.0;
        this.it = 0.0;
        this.intermediaryCommission = 0.0;
        this.intermediaryCommissionPercentage = 0.0;
        this.collectionServiceCommission = 0.0;
        this.collectionServiceCommissionPercentage = 0.0;
        this.insuredCapital = insuredCapital;
        this.policyStatusIdc = policyStatusIdc;
        this.branchOfficeId = null;
        this.agencyId = null;
        this.renewal = 0;
        this.assignedExecutiveId = null;
        this.intermediaryTypeIdc = 0;
        this.brokerId = null;
        this.agentId = null;
        this.productId = productId;
    }

    // VIN - Constructor
    public Policy(Long generalRequestId, Integer currencyTypeIdc, Double premium, Double insuredCapital,
                  Integer policyStatusIdc, Long productId, Integer termInDays) {

        Date validateFrom = DateUtils.asDate(LocalDate.now());
        Date issuanceDate = new Date();
        Date validated = DateUtils.asSummaryRestartDays(validateFrom, Calendar.DAY_OF_YEAR, termInDays);

        this.generalRequestId = generalRequestId;
        this.currencyTypeIdc = currencyTypeIdc;
        this.exchangeRate = 0;
        this.correlativeNumber = 0L;
        this.numberPolicy = "SP-" + HelpersMethods.formatStringDate(issuanceDate);
        this.issuanceDate = issuanceDate;
        this.fromDate = DateUtils.asLocalDateTimeStamp(DateUtils.asDateToLocalDate(validateFrom));
        this.toDate = DateUtils.asLocalDateTimeStamp(DateUtils.asDateToLocalDate(validateFrom));
        this.totalPremium = premium;
        this.additionalPremium = 0.0;
        this.netPremium = 0.0;
        this.riskPremium = 0.0;
        this.aps = 0.0;
        this.iva = 0.0;
        this.it = 0.0;
        this.intermediaryCommission = 0.0;
        this.intermediaryCommissionPercentage = 0.0;
        this.collectionServiceCommission = 0.0;
        this.collectionServiceCommissionPercentage = 0.0;
        this.insuredCapital = insuredCapital;
        this.policyStatusIdc = policyStatusIdc;
        this.branchOfficeId = null;
        this.agencyId = null;
        this.renewal = 0;
        this.assignedExecutiveId = null;
        this.intermediaryTypeIdc = 0;
        this.brokerId = null;
        this.agentId = null;
        this.productId = productId;
    }
    //#endregion

}
