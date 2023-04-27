package com.scfg.core.adapter.persistence.fabolousAccount.fabolousZone;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = HelpersConstants.TABLE_FBS_ZONE)
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class FabolousZoneJpaEntity extends BaseJpaEntity {

    private String zoneName;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousBranchId")
//    private FabolousBranchJpaEntity fabolousBranch;

    @Column(name = "fabolousBranchId")
    private Long fabolousBranch;
}
