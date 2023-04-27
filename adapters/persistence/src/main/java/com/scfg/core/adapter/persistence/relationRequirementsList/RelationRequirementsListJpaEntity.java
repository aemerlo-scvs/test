package com.scfg.core.adapter.persistence.relationRequirementsList;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.requirementsTable.RequirementsTableJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "RelationRequirementsList")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class RelationRequirementsListJpaEntity extends BaseJpaEntity {
    @Column(name = "requirementIdc")
    private Long requirementIdc;

    @Column(name = "requirementTableId")
    private Long requirementTableId;
}
