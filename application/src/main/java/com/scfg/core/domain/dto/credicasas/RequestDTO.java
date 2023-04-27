package com.scfg.core.domain.dto.credicasas;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestDTO {

    private String identificationNumber;

    private String complement;

    private String names;

    private String lastname;

    private String mothersLastname;

    private String marriedLastname;

    private Long requestId;

    private Integer requestNumber;

    private Date requestDate;

    private Integer requestStatusIdc;

    private Integer acceptanceReasonIdc;

    private Integer rejectedReasonIdc;

    private String pendingReason;

    private Double requestedAmount;

    private Double currentAmount;

    private Double accumulatedAmount;

    private LocalDateTime pronouncementDate;

    @Override
    public String toString() {
        return "RequestDTO{" +
                "identificationNumber='" + identificationNumber + '\'' +
                ", complement='" + complement + '\'' +
                ", names='" + names + '\'' +
                ", lastname='" + lastname + '\'' +
                ", mothersLastname='" + mothersLastname + '\'' +
                ", marriedLastname='" + marriedLastname + '\'' +
                ", requestId=" + requestId +
                ", requestNumber=" + requestNumber +
                ", requestDate=" + requestDate +
                ", requestStatusIdc=" + requestStatusIdc +
                ", acceptanceReasonIdc=" + acceptanceReasonIdc +
                ", rejectedReasonIdc=" + rejectedReasonIdc +
                ", pendingReason='" + pendingReason + '\'' +
                ", requestedAmount=" + requestedAmount +
                ", currentAmount=" + currentAmount +
                ", accumulatedAmount=" + accumulatedAmount +
                ", pronouncementDate=" + pronouncementDate +
                '}';
    }
}
