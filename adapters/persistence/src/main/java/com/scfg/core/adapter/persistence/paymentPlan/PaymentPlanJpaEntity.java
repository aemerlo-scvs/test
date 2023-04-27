package com.scfg.core.adapter.persistence.paymentPlan;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "PaymentPlan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PaymentPlanJpaEntity extends BaseJpaEntity {

    @Column(name = "quoteNumber")
    private Integer quoteNumber;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "amountPaid")
    private Double amountPaid;

    @Column(name = "residue")
    private Double residue;

    @Column(name = "percentage")
    private Integer percentage;

    @Column(name = "expirationDate")
    private LocalDateTime expirationDate;

    @Column(name = "rescheduleDate")
    private LocalDateTime rescheduleDate;

    @Column(name = "datePaid")
    private LocalDateTime datePaid;

    @Column(name = "paymentId")
    private Long paymentId;

}
