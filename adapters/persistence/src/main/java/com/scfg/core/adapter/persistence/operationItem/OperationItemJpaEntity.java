package com.scfg.core.adapter.persistence.operationItem;

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
@Table(name = "OperationItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OperationItemJpaEntity extends BaseJpaEntity {

    @Column(name = "operationTerm", nullable = false)
    private Integer operationTerm;

    @Column(name = "currentAmount", nullable = false)
    private Float currentAmount;

    @Column(name = "monthIdc", nullable = false)
    private Integer monthIdc;

    @Column(name = "yearIdc", nullable = false)
    private Integer yearIdc;

    @Column(name = "operationHeaderId", nullable = false)
    private Long operationHeaderId;
}
