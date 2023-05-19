package com.scfg.core.adapter.persistence.payment;

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
@Table(name = "Payment")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PaymentJpaEntity extends BaseJpaEntity {

    @Column(name = "paymentTypeIdc")
    private Integer paymentTypeIdc;

    @Column(name = "paymentPeriod")
    private Integer paymentPeriod;

    @Column(name = "total")
    private Double total;

    @Column(name = "surcharge")
    private Double surcharge;

    @Column(name = "totalSurcharge")
    private Double totalSurcharge;

    @Column(name = "totalPaid")
    private Double totalPaid;

    @Column(name = "currencyTypeIdc")
    private Integer currencyTypeIdc;

    @Column(name = "quoteQuantity")
    private Integer quoteQuantity;

    @Column(name = "startDate")
    private LocalDateTime startDate;

    @Column(name = "endDate")
    private LocalDateTime endDate;

    @Column(name = "generalRequestId")
    private Long generalRequestId;

    @Column(name = "annexeId")
    private Long annexeId;

    @Column(name = "paymentFileDocumentId")
    private Long paymentFileDocumentId;

}
