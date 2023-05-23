package com.scfg.core.domain;

import com.scfg.core.common.enums.PaymentTypeEnum;
import com.scfg.core.common.enums.PeriodicityEnum;
import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Payment extends BaseDomain {

    private Integer paymentTypeIdc;
    private Integer paymentPeriod;
    private Double total;
    private Double surcharge;
    private Double totalSurcharge;
    private Double totalPaid;
    private Integer currencyTypeIdc;
    private Integer quoteQuantity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long generalRequestId;
    private Long annexeId;
    private Long paymentFileDocumentId;

    //#region Constructors

    public Payment(double totalPayment, int currencyType, long generalRequestId, long annexeId) {
        this.paymentTypeIdc = PaymentTypeEnum.Cash.getValue();
        this.paymentPeriod = PeriodicityEnum.None.getValue();
        this.total = totalPayment;
        this.surcharge = 0.00;
        this.totalSurcharge = 0.00;
        this.totalPaid = totalPayment;
        this.currencyTypeIdc = currencyType;
        this.generalRequestId = generalRequestId;
        this.annexeId = annexeId;
    }

    //#endregion
}
