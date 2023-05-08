package com.scfg.core.domain;

import com.scfg.core.common.enums.PaymentChannelEnum;
import com.scfg.core.common.enums.TransactionTypeEnum;
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
public class Transaction extends BaseDomain {

    private Double amount;

    private Double remainAmount;

    private LocalDateTime datePaid;

    private Integer currencyTypeIdc;

    private Double exchangeRate;

    private LocalDateTime annulmentDate;

    private String annulmentReason;

    private Integer paymentChannelIdc;

    private Integer transactionType;

    private String observation;

    private Long paymentPlanId;

    private Long documentPaymentId;

    private String voucherNumber;

    private Long transactionFileDocumentId;


    //#region Constructors

    public Transaction(Double amount, LocalDateTime currentDate, Integer currencyTypeIdc, Long paymentPlanId,
                       String voucherNumber, Long createdBy, Long lastModifiedBy) {
        this.amount = amount;
        this.remainAmount = 0.00;
        this.datePaid = currentDate;
        this.currencyTypeIdc = currencyTypeIdc;
        this.paymentChannelIdc = PaymentChannelEnum.Cash.getValue();
        this.transactionType = TransactionTypeEnum.PremiumPayment.getValue();
        this.paymentPlanId = paymentPlanId;
        this.voucherNumber = voucherNumber;

        this.setCreatedBy(createdBy);
        this.setLastModifiedBy(lastModifiedBy);
    }

    public Transaction(Double amount, LocalDateTime currentDate, Integer currencyTypeIdc, Long paymentPlanId,
                       String voucherNumber) {
        this.amount = amount;
        this.remainAmount = 0.00;
        this.datePaid = currentDate;
        this.currencyTypeIdc = currencyTypeIdc;
        this.paymentChannelIdc = PaymentChannelEnum.Cash.getValue();
        this.transactionType = TransactionTypeEnum.PremiumPayment.getValue();
        this.paymentPlanId = paymentPlanId;
        this.voucherNumber = voucherNumber;
    }


    //#endregion

}
