package com.scfg.core.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class InsuranceRequest extends BaseDomain {

    private String requestNumber;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate insuranceRequestDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate schedulingDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fulfillmentRequirementsDate;

    private Double requestedAmount;

    private Double accumulatedAmount;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate disbursementDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate exclusionDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate inclusionDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate djsFillDate; // fecha de llenado

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate pronouncementDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate bankPronouncementDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate djsReceptionDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate acceptanceDate;

    // Relationship (By Identifiers)
    private Long requestStatusId;
    private Long currencyId;

    // Relationships (By Objects)
    private Classifier requestStatus;

    private Classifier currency;
}
