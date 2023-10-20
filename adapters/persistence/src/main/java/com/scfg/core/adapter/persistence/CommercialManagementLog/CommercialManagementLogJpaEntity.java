package com.scfg.core.adapter.persistence.CommercialManagementLog;

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
@Table(name = "CommercialManagementLog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CommercialManagementLogJpaEntity extends BaseJpaEntity {
    @Column(name = "idCommercialManagement")
    private Long idCommercialManagement;

    @Column(name = "comment")
    private String comment;
}
