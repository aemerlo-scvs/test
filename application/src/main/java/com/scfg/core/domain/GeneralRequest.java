package com.scfg.core.domain;

import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.domain.common.BaseDomain;
import com.scfg.core.domain.dto.credicasas.groupthefont.requestDto.RequestFontDTO;
import com.scfg.core.domain.dto.vin.VinProcessRequestDTO;
import com.scfg.core.domain.person.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.*;
import java.util.Date;
import java.util.Locale;

@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class GeneralRequest extends BaseDomain {

    private LocalDateTime requestDate;

    private String description;

    private Double weight;

    private Double height;

    private String creditNumber;

    private Integer requestNumber;

    private Double requestedAmount;

    private Double currentAmount;

    private Double accumulatedAmount;

    private Integer creditTerm;

    private Integer creditTermInYears;

    private Integer creditTermInDays;

    private Integer acceptanceReasonIdc;

    private Integer rejectedReasonIdc;

    private String pendingReason;

    private String exclusionComment;

    private String rejectedComment;

    private String inactiveComment;

    private Integer legalHeirs;

    private String activationCode;

    private Integer requestStatusIdc;

    private Long planId;

    private Long personId;

    private Integer insuredTypeIdc;

    private Long typeIdc;

    //#region Custom Constructor

    public GeneralRequest() {
    }

    //CLF
    public GeneralRequest (RequestFontDTO credit, Person person, int status, long planId, int acceptanceReasonIdc, int rejectedReasonIdc,
                           String pendingReason, String exclusionComment, String rejectedComment, int legalHeirs) {

        String requestDescription = "Solicitud del cliente: " + person.getNaturalPerson().getCompleteName();
        this.setId(0L);
        this.setRequestDate(LocalDateTime.now());
        this.setDescription(requestDescription);
        this.setWeight(credit.getAcceptanceCriteria().getWeight());
        this.setHeight(credit.getAcceptanceCriteria().getHeight());
        this.setCreditNumber(credit.getCredit().getCreditNumber());
        this.setRequestedAmount(credit.getCredit().getRequestedAmount());
        this.setCurrentAmount(credit.getCredit().getValidAmount());
        this.setAccumulatedAmount(credit.getCredit().getAccumulatedAmount());
        this.setCreditTerm(credit.getCredit().getCreditTerm());
        this.setRejectedReasonIdc(rejectedReasonIdc);
        this.setAcceptanceReasonIdc(acceptanceReasonIdc);
        this.setPendingReason(pendingReason.toUpperCase(Locale.ROOT));
        this.setExclusionComment(exclusionComment.toUpperCase(Locale.ROOT));
        this.setRejectedComment(rejectedComment.toUpperCase(Locale.ROOT));
        this.setLegalHeirs(legalHeirs);
        this.setActivationCode("");
        this.setRequestStatusIdc(status);
        this.setPersonId(person.getId());
        this.setInsuredTypeIdc(credit.getCredit().getInsuredTypeIdc().intValue());
        this.setPlanId(planId);
        this.setTypeIdc(ClassifierEnum.TYPE_REQUEST_REQUEST.getReferenceCode());
    }

    //VIN
    public GeneralRequest(VinProcessRequestDTO processRequest, int status, long personId, long planId) {
        String requestDescription = "Propuesta del cliente: " + processRequest.getPerson().getNaturalPerson().getCompleteName();

        LocalDateTime requestDate;
        if(processRequest.getRequestDate()!=null)
            requestDate=processRequest.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        else
            requestDate=new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        int daysInYear = 0;
        if (processRequest.getTermInYears() == 1) {
            daysInYear = 370;
        }
        else
        {
            int actualYear=requestDate.toInstant(ZoneOffset.UTC).atZone(ZoneId.systemDefault()).getYear();
            for (int i=0;i<processRequest.getTermInYears();i++){
                if(LocalDate.of(actualYear+i, Month.JANUARY,1).lengthOfYear()==365)
                {
                    daysInYear+=365;
                }
                else
                {
                    if(requestDate.getMonthValue()>2)
                    {
                        daysInYear+=365;
                    }
                    else
                    {
                        daysInYear+=366;
                    }
                }
            }
        }
        this.setId(0L);
        this.setRequestDate(requestDate);
        this.setDescription(requestDescription);
        this.setWeight(null);
        this.setHeight(null);
        this.setCreditNumber(processRequest.getOperationNumber());
        this.setRequestedAmount(null);
        this.setCurrentAmount(null);
        this.setAccumulatedAmount(null);
        this.setCreditTerm(processRequest.getCreditTerm());
        this.setCreditTermInDays(daysInYear);
        this.setCreditTermInYears(processRequest.getTermInYears());
        this.setRejectedReasonIdc(null);
        this.setAcceptanceReasonIdc(null);
        this.setPendingReason("Propuesta pendiente de confirmación");
        this.setExclusionComment(null);
        this.setRejectedComment(null);
        this.setLegalHeirs(null);
        this.setActivationCode(null);
        this.setRequestStatusIdc(status);
        this.setPersonId(personId);
        this.setInsuredTypeIdc(null);
        this.setPlanId(planId);
        this.setTypeIdc(ClassifierEnum.TYPE_REQUEST_PROPOSAL.getReferenceCode());
    }

    //#endregion

}
