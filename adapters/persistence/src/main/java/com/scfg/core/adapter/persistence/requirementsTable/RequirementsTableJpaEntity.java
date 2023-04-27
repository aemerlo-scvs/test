package com.scfg.core.adapter.persistence.requirementsTable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.plan.PlanJpaEntity;
import com.scfg.core.adapter.persistence.product.ProductJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RequirementsTable")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class RequirementsTableJpaEntity extends BaseJpaEntity {

    @Column(name = "startAge")
    private Double startAge;

    @Column(name = "finishAge")
    private Double finishAge;

    @Column(name = "initialAmount")
    private Double initialAmount;

    @Column(name = "finalAmount")
    private Double finalAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "planId")
    private Long planId;
}
