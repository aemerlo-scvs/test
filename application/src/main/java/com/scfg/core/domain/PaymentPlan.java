package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PaymentPlan extends BaseDomain {

    private Integer quoteNumber;

    private Double amount;

    private Double amountPaid;

    private Double residue;

    private Integer percentage;

    private LocalDateTime expirationDate;

    private LocalDateTime rescheduleDate;

    private LocalDateTime datePaid;

    private Long paymentId;

    private Long annexeId;


    //#region Constructors

    public PaymentPlan(Double amount, Long paymentId, LocalDateTime currentDate, Long createdBy, Long lastModifiedBy) {
        this.quoteNumber = 1;
        this.amount = amount;
        this.amountPaid = amount;
        this.residue = 0.0;
        this.percentage = 100;
        this.expirationDate = currentDate;
        this.datePaid = currentDate;
        this.paymentId = paymentId;

        this.setCreatedBy(createdBy);
        this.setLastModifiedBy(lastModifiedBy);
    }

    public PaymentPlan(Double amount, Long paymentId, LocalDateTime currentDate, Long annexeId) {
        this.quoteNumber = 1;
        this.amount = amount;
        this.amountPaid = amount;
        this.residue = 0.0;
        this.percentage = 100;
        this.expirationDate = currentDate;
        this.datePaid = currentDate;
        this.paymentId = paymentId;
        this.annexeId = annexeId;
    }


    //#endregion
}
