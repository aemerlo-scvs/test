package com.scfg.core.adapter.persistence.policyItemEconomicReinsurance;

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
@Table(name = "PolicyItemEconomicReinsurance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PolicyItemEconomicReinsuranceJpaEntity extends BaseJpaEntity {
    @Column(name = "policyItemEconomicId", nullable = false)
    private Long policyItemEconomicId;
    @Column(name = "premiumCeded", nullable = false)
    private Double premiumCeded;
    @Column(name = "coverageId", nullable = false)
    private Long coverageId;
    @Column(name = "reinsurerId", nullable = false)
    private Long reinsurerId;
}
