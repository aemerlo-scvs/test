package com.scfg.core.adapter.persistence.generalRequest;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GeneralRequest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class GeneralRequestJpaEntity extends BaseJpaEntity {

    @Column(name = "requestNumber")
    private Integer requestNumber;

    @Column(name = "requestDate")
    private LocalDateTime requestDate;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "creditNumber", length = 100)
    private String creditNumber;

    @Column(name = "requestedAmount")
    private Double requestedAmount;

    @Column(name = "currentAmount")
    private Double currentAmount;

    @Column(name = "accumulatedAmount")
    private Double accumulatedAmount;

    @Column(name = "creditTerm")
    private Integer creditTerm;

    @Column(name = "creditTermInYears")
    private Integer creditTermInYears;

    @Column(name = "creditTermInDays")
    private Integer creditTermInDays;

    @Column(name = "acceptanceReasonIdc")
    private Integer acceptanceReasonIdc;

    @Column(name = "rejectedReasonIdc")
    private Integer rejectedReasonIdc;

    @Column(name = "pendingReason")
    private String pendingReason;

    @Column(name = "exclusionComment")
    private String exclusionComment;

    @Column(name = "rejectedComment")
    private String rejectedComment;

    @Column(name = "inactiveComment")
    private String inactiveComment;

    @Column(name = "legalHeirs")
    private Integer legalHeirs;

    @Column(name = "activationCode", length = 100)
    private String activationCode;

    @Column(name = "requestStatusIdc")
    private Integer requestStatusIdc;

    @Column(name = "insuredTypeIdc")
    private Integer insuredTypeIdc;

    @Column(name = "planId")
    private Long planId;

    @Column(name = "personId")
    private Long personId;

    @Column(name = "typeIdc")
    private Long typeIdc;
}
