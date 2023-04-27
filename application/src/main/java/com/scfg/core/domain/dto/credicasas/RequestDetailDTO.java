package com.scfg.core.domain.dto.credicasas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RequestDetailDTO {

    private Long id;
    private Integer requestNumber;
    private Integer requestStatusIdc;
    private String requestStatusDescription;
    private LocalDateTime requestDate;
    private Integer insuredTypeIdc;
    private String creditNumber;
    private Integer creditTerm;
    private Double height;
    private Double weight;
    private String name;
    private String lastName;
    private String motherLastName;
    private String marriedLastName;
    private String identificationNumber;
    private String activationCode;
    private String complement;
    private Integer extIdc;
    private String extDescription;
    private Integer maritalStatusIdc;
    private Integer workTypeIdc;
    private Double requestedAmount; //Monto solicitado
    private Double currentAmount; //Monto vigente
    private Double accumulatedAmount; //Monto acumulado
    private Double actualAccumulatedAmount; //Monto acumulado actual
    private Double actualCurrentAmount; //Saldo deudor
    private String organizationName;
    private LocalDateTime birthDate;
    private Boolean isPep;
    private Long planId;
    private Long policyItemId;
    private LocalDateTime pronouncementDate;
    private Integer acceptanceReasonIdc;
    private Integer rejectedReasonIdc;
    private String pendingReason;
    private String exclusionComment;
    private String rejectedComment;

    private String inactiveComment;
    private Long createdBy;
    private Date createdAt;

    private Date lastModifiedAt;
    private String coverages;

    public RequestDetailDTO(Long id, Integer requestNumber, Integer requestStatusIdc, String requestStatusDescription, LocalDateTime requestDate, Integer insuredTypeIdc, String creditNumber, Integer creditTerm, Double height, Double weight,
                            String name, String lastName, String motherLastName, String marriedLastName, String identificationNumber, String activationCode, String complement, Integer extIdc, String extDescription, Integer maritalStatusIdc,
                            Double requestedAmount, Double currentAmount, Double accumulatedAmount, Double actualAccumulatedAmount, Double actualCurrentAmount,
                            String organizationName, LocalDateTime birthDate, Boolean isPep, Long planId, Long policyItemId, LocalDateTime pronouncementDate, Integer workTypeIdc,
                            Integer acceptanceReasonIdc, Integer rejectedReasonIdc, String pendingReason, String exclusionComment, String rejectedComment, String inactiveComment, Long createdBy, Date createdAt, Date lastModifiedAt, String coverages) {
        this.id = id;
        this.requestNumber = requestNumber;
        this.requestStatusIdc = requestStatusIdc;
        this.requestStatusDescription = requestStatusDescription;
        this.requestDate = requestDate;
        this.insuredTypeIdc = insuredTypeIdc;
        this.creditNumber = creditNumber;
        this.creditTerm = creditTerm;
        this.height = height;
        this.weight = weight;
        this.name = name;
        this.lastName = lastName;
        this.motherLastName = motherLastName;
        this.marriedLastName = marriedLastName;
        this.identificationNumber = identificationNumber;
        this.activationCode = activationCode;
        this.complement = complement;
        this.extIdc = extIdc;
        this.extDescription = extDescription;
        this.maritalStatusIdc = maritalStatusIdc;
        this.requestedAmount = requestedAmount;
        this.currentAmount = (currentAmount != null) ? currentAmount : 0;
        this.accumulatedAmount = (accumulatedAmount != null) ? accumulatedAmount : 0;
        this.actualAccumulatedAmount = (actualAccumulatedAmount != null) ? actualAccumulatedAmount : 0;
        this.actualCurrentAmount = (actualCurrentAmount != null) ? actualCurrentAmount : 0;
        this.organizationName = organizationName;
        this.birthDate = birthDate;
        this.isPep = isPep;
        this.planId = planId;
        this.policyItemId = policyItemId;
        this.pronouncementDate = pronouncementDate;
        this.workTypeIdc = workTypeIdc;
        this.acceptanceReasonIdc = acceptanceReasonIdc;
        this.rejectedReasonIdc = rejectedReasonIdc;
        this.pendingReason = pendingReason;
        this.exclusionComment = exclusionComment;
        this.rejectedComment = rejectedComment;
        this.inactiveComment = inactiveComment;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.coverages = coverages;
    }
}
