package com.scfg.core.adapter.persistence.policyItemMathReserve;

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
@Table(name = "PolicyItemMathReserve")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PolicyItemMathReserveJpaEntity extends BaseJpaEntity {

    @Column(name = "insuranceValidity", nullable = false)
    private Integer insuranceValidity;

    @Column(name = "[year]", nullable = false)
    private Integer year;

    @Column(name = "[value]", nullable = false)
    private Float value;

    @Column(name = "policyItemId")
    private Long policyItemId;
}
