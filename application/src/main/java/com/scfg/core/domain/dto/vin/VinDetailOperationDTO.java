package com.scfg.core.domain.dto.vin;

import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.Beneficiary;
import lombok.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VinDetailOperationDTO {

    private String identificationNumber;
    private String complement;
    private String names;
    private String lastname;
    private String mothersLastname;
    private String marriedLastname;
    private Long generalRequestId;
    private Integer policyStatusIdc;
    private String policyNumber;
    private Long policyId;
    private Integer requestNumber;
    private Integer requestStatusIdc;
    private Integer termInYears;
    private String productName;
    private String planName;
    private String operationNumber;
    private Long policyItemId;
    private Double totalPremium;
    private List<Beneficiary>beneficiaryList;
    private Date initialValidity;
    private Date endValidity;

    public VinDetailOperationDTO(
            String identificationNumber, String complement, String name, String lastname, String mothersLastname, String marriedLastname,
            Long generalRequestId, String creditNumber, Integer requestNumber, Integer requestStatusIdc, Long policyId, Integer policyStatusIdc,
            String policyNumber, Double totalPremium, String productName, String planName, Integer creditTermInYears, Long policyItemId ) {
        this.operationNumber = creditNumber;
        this.identificationNumber = identificationNumber;
        this.complement = complement;
        this.names = name;
        this.lastname = lastname;
        this.mothersLastname = mothersLastname;
        this.marriedLastname = marriedLastname;
        this.generalRequestId = generalRequestId;
        this.requestNumber = requestNumber;
        this.requestStatusIdc = requestStatusIdc;
        this.policyId = policyId;
        this.policyStatusIdc = policyStatusIdc;
        this.policyNumber = policyNumber;
        this.termInYears = creditTermInYears;
        this.productName = productName;
        this.planName = planName;
        this.policyItemId = policyItemId;
        this.totalPremium = totalPremium;
        this.initialValidity = new Date();
        this.endValidity = addYears(new Date(), 1);
    }

    public Date addYears(Date date, int years) {
        Calendar calDateStart = Calendar.getInstance();
        calDateStart.setTime(date);
        calDateStart.add(Calendar.YEAR, years);
        return calDateStart.getTime();
    }

}
