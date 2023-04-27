package com.scfg.core.adapter.persistence.operationHeader;

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
@Table(name = "OperationHeader")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OperationHeaderJpaEntity extends BaseJpaEntity {

    @Column(name = "operationNumber", nullable = false)
    private String operationNumber;

    @Column(name = "operationTypeIdc", nullable = false)
    private Integer operationTypeIdc;

    @Column(name = "disbursedAmount")
    private Float disbursedAmount;

    @Column(name = "disbursedDate")
    private LocalDateTime disbursedDate;

    @Column(name = "policyItemId", nullable = false)
    private Long policyItemId;

    @Column(name = "parentId")
    private Long parentId;
}
