package com.scfg.core.adapter.persistence.coveragePolicyItem;

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

@Entity
@Table(name = "CoveragePolicyItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class CoveragePolicyItemJpaEntity extends BaseJpaEntity {

    @Column(name = "policyItemId")
    private Long policyItemId;

    @Column(name = "coverageProductPlanId")
    private Long coverageProductPlanId;

    @Column(name = "insuredCapital")
    private Double insuredCapital;

    @Column(name = "additionalPremiumPerPercentage")
    private Double additionalPremiumPerPercentage;

    @Column(name = "additionalPremiumPerThousand")
    private Double additionalPremiumPerThousand;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "coverageId")
    private Long coverageId;

    @Column(name = "insuredCapitalCededRate")
    private Double insuredCapitalCededRate;

    @Column(name = "insuredCapitalCeded")
    private Double insuredCapitalCeded;

    @Column(name = "insuredCapitalRetained")
    private Double insuredCapitalRetained;

    @Column(name = "IRE")
    private Double IRE;

    @Column(name = "reinsurerId")
    private Long reinsurerId;

}
