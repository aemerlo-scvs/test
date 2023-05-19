package com.scfg.core.adapter.persistence.annexeRequest;

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
@Table(name = "RequestAnnexe")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnnexeRequestJpaEntity extends BaseJpaEntity {
    @Column(name = "reasonIdc")
    private Integer reasonIdc;
    @Column(name = "statusIdc")
    private Integer statusIdc;
    @Column(name = "comment")
    private String comment;
    @Column(name = "annexeTypeId")
    private Long annexeTypeId;
    @Column(name = "policyId")
    private Long policyId;
    @Column(name = "requestDate")
    private LocalDateTime requestDate;
}
