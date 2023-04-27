package com.scfg.core.adapter.persistence.requirementControl;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "RequirementControl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class RequirementControlJpaEntity extends BaseJpaEntity {

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "requestDate")
    private LocalDate requestDate;

    @Column(name = "receptionDate")
    private LocalDate receptionDate;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "requirementId")
    private Long requirementId;

    @Column(name = "policyItemId")
    private Long policyItemId;

    @Column(name = "fileDocumentId")
    private Long fileDocumentId;
}
