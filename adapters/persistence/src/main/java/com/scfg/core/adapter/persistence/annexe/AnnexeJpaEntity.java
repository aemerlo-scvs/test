package com.scfg.core.adapter.persistence.annexe;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Annexe")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnnexeJpaEntity extends BaseJpaEntity {
    @Column(name = "annexeNumber")
    private Integer annexeNumber;
    @Column(name = "annexeTypeIdc")
    private Integer annexeTypeIdc;
    @Column(name = "startDate")
    private LocalDateTime startDate;
    @Column(name = "endDate")
    private LocalDateTime endDate;
    @Column(name = "issuanceDate")
    private LocalDateTime issuanceDate;
    @Column(name = "policyId")
    private Long policyId;
    @Column(name = "requestAnnexeId")
    private Long requestAnnexeId;
}
