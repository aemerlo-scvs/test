package com.scfg.core.adapter.persistence.coveragePlan;

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
@Table(name = "CoveragePlan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class CoveragePlanJpaEntity extends BaseJpaEntity {
    @Column(name = "planId")
    private Long planId;
    @Column(name = "coverageId")
    private Long coverageId;
    @Column(name = "insuredCapital")
    private float insuredCapital;

}
