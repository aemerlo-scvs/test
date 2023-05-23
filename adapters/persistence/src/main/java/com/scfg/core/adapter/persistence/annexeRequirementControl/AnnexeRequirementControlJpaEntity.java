package com.scfg.core.adapter.persistence.annexeRequirementControl;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "AnnexeRequirementControl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnnexeRequirementControlJpaEntity extends BaseJpaEntity {
    @Column(name = "description")
    private String description;
    @Column(name = "comment")
    private String comment;
    @Column(name = "requestDate")
    private LocalDateTime requestDate;
    @Column(name = "receptionDate")
    private LocalDateTime receptionDate;
    @Column(name = "fileDocumentId")
    private Long fileDocumentId;
    @Column(name = "requirementId")
    private Long requirementId;
    @Column(name = "requestAnnexeId")
    private Long requestAnnexeId;

    @Column(name = "signed")
    private Boolean signed;

}
