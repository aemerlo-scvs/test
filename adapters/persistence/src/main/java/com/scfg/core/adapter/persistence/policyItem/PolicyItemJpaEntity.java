package com.scfg.core.adapter.persistence.policyItem;

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
import java.util.Date;

@Entity
@Table(name = "PolicyItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PolicyItemJpaEntity extends BaseJpaEntity {

    @Column(name = "personId", nullable = false)
    private Long personId;

    @Column(name = "policyId", nullable = false)
    private Long policyId;

    @Column(name = "generalRequestId")
    private Long generalRequestId;

    @Column(name = "individualInsuredCapital", nullable = false)
    private Double individualInsuredCapital;

    @Column(name = "validityStart", nullable = false)
    private Date validityStart;

    @Column(name = "termValidity", nullable = false)
    private Date termValidity;

    @Column(name = "individualPremium")
    private Double individualPremium;

    @Column(name = "individualPremiumRate")
    private Double individualPremiumRate;

    @Column(name = "riskPositionIdc")
    private Integer riskPositionIdc;

    @Column(name = "pronouncementDate")
    private LocalDateTime pronouncementDate;

    @Column(name = "individualAdditionalPremium")
    private Double individualAdditionalPremium;

    @Column(name = "individualNetPremium")
    private Double individualNetPremium;

    @Column(name = "individualRiskPremium")
    private Double individualRiskPremium;

    @Column(name = "APS")
    private Double APS;

    @Column(name = "FPA")
    private Double FPA;

    @Column(name = "IVA")
    private Double IVA;

    @Column(name = "IT")
    private Double IT;

    @Column(name = "individualIntermediaryCommission")
    private Double individualIntermediaryCommission;

    @Column(name = "individualCollectionServiceCommission")
    private Double individualCollectionServiceCommission;

    @Column(name = "annexeId")
    private Long annexeId;

    @Column(name = "itemStatusIdc")
    private Integer itemStatusIdc;

    @Column(name = "inclusionDate")
    private LocalDateTime inclusionDate;

    @Column(name = "exclusionDate")
    private LocalDateTime exclusionDate;
}
