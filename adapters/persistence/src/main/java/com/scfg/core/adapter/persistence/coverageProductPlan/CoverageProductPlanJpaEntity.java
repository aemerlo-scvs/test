package com.scfg.core.adapter.persistence.coverageProductPlan;

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
@Table(name = "CoverageProductPlan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class CoverageProductPlanJpaEntity extends BaseJpaEntity {

    @Column(name = "insuredCapitalCoverage")
    private Double insuredCapitalCoverage;

    // Cambiar esto en una relación influye en la consulta de ProductRepository
    @Column(name = "coverageProductId")
    private Long coverageProductId;

    // Cambiar esto en una relación influye en la consulta de ProductRepository
    @Column(name = "planId")
    private Long planId;

    @Column(name = "minimumEntryAge")
    private Integer minimumEntryAge;

    @Column(name = "entryAgeLimit")
    private Integer entryAgeLimit;

    @Column(name = "ageLimitStay")
    private Integer ageLimitStay;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "parentCoverageProductId")
    private Long parentCoverageProductId;

    @Column(name = "[order]")
    private Integer order;
}
