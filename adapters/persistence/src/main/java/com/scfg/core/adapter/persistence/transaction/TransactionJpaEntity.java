package com.scfg.core.adapter.persistence.transaction;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "[Transaction]")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class TransactionJpaEntity extends BaseJpaEntity {

    @Column(name = "amount")
    private Double amount;

    @Column(name = "remainAmount")
    private Double remainAmount;

    @Column(name = "datePaid")
    private LocalDateTime datePaid;

    @Column(name = "currencyTypeIdc")
    private Integer currencyTypeIdc;

    @Column(name = "exchangeRate")
    private Double exchangeRate;

    @Column(name = "annulmentDate")
    private LocalDateTime annulmentDate;

    @Column(name = "annulmentReason", length = 500)
    private String annulmentReason;

    @Column(name = "paymentChannelIdc")
    private Integer paymentChannelIdc;

    @Column(name = "transactionType")
    private Integer transactionType;

    @Column(name = "observation", length = 500)
    private String observation;

    @Column(name = "paymentPlanId")
    private Long paymentPlanId;

    @Column(name = "documentPaymentId")
    private Long documentPaymentId;

    @Column(name = "voucherNumber")
    private String voucherNumber;

    @Column(name = "transactionFileDocumentId")
    private Long transactionFileDocumentId;

}
