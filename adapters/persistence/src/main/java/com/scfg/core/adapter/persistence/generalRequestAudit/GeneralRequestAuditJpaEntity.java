package com.scfg.core.adapter.persistence.generalRequestAudit;

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
@Table(name = "GeneralRequestAudit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GeneralRequestAuditJpaEntity extends BaseJpaEntity {
    @Column(name = "oldStatusIdc")
    private Integer oldStatusIdc;

    @Column(name = "newStatusIdc")
    private Integer newStatusIdc;

    @Column(name = "generalRequestId")
    private Long generalRequestId;
}
