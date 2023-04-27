package com.scfg.core.adapter.persistence.annexeRequirement;

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
@Table(name = "AnnexeRequirement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnnexeRequirementJpaEntity extends BaseJpaEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "annexeTypeId")
    private Long annexeTypeId;
}
