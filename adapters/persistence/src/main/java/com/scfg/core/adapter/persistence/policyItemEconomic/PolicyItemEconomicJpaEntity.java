package com.scfg.core.adapter.persistence.policyItemEconomic;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PolicyItemEconomic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PolicyItemEconomicJpaEntity extends BaseJpaEntity {
    @Column(name = "policyItemId", nullable = false)
    private Long policyItemId;
    @Column(name = "annexeId")
    private Long annexeId;
    @Column(name = "movementTypeIdc", nullable = false)
    private Integer movementTypeIdc;
    @Column(name = "movementSubTypeIdc", nullable = false)
    private Integer movementSubTypeIdc;
    @Column(name = "exchangeRate", nullable = false)
    private Double exchangeRate;
    @Column(name = "individualPremium", nullable = false)
    private Double individualPremium;
    @Column(name = "individualNetPremium", nullable = false)
    private Double individualNetPremium;
    @Column(name = "individualAdditionalPremium", nullable = false)
    private Double individualAdditionalPremium;
    @Column(name = "individualRiskPremium")
    private Double individualRiskPremium;
    @Column(name = "APS", nullable = false)
    private Double APS;
    @Column(name = "FPA", nullable = false)
    private Double FPA;
    @Column(name = "IVA")
    private Double IVA;
    @Column(name = "IT")
    private Double IT;
    @Column(name = "intermediaryTypeIdc", nullable = false)
    private Double intermediaryTypeIdc;
    @Column(name = "intermediaryId")
    private Long intermediaryId;
    @Column(name = "individualIntermediaryCommissionIndicatorIdc", nullable = false)
    private Integer individualIntermediaryCommissionIndicatorIdc;
    @Column(name = "individualIntermediaryCommissionPercentage")
    private Double individualIntermediaryCommissionPercentage;
    @Column(name = "individualIntermediaryCommission")
    private Double individualIntermediaryCommission;
    @Column(name = "individualCollectionServiceCommissionIndicatorIdc")
    private Integer individualCollectionServiceCommissionIndicatorIdc;
    @Column(name = "individualCollectionServiceCommission")
    private Double individualCollectionServiceCommission;
    @Column(name = "individualPremiumCeded")
    private Double individualPremiumCeded;
    @Column(name = "individualPremiumRetained")
    private Double individualPremiumRetained;
}
