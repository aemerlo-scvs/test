package com.scfg.core.adapter.persistence.requestAnnexe;

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
@Table(name = "RequestAnnexe")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RequestAnnexeJpaEntity  extends BaseJpaEntity {
    @Column(name = "description")
    private String description;
   @Column(name = "annulmentReason")
    private String annulmentReason;
   @Column(name = "statusIdc")
    private Integer statusIdc;
   @Column(name = "annexeTypeId")
    private Long annexeTypeId;
   @Column(name = "policyId")
    private Long policyId;
}
