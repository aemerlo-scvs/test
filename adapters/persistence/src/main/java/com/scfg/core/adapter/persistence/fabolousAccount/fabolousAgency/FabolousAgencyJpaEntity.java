package com.scfg.core.adapter.persistence.fabolousAccount.fabolousAgency;

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
@Table(name = HelpersConstants.TABLE_FBS_AGENCY)
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class FabolousAgencyJpaEntity extends BaseJpaEntity {

    private String agencyName;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousZoneId")
//    @JoinColumn(name = "fabolousZoneId")
//    @JsonBackReference
//    private FabolousZoneJpaEntity fabolousZone;

    @Column(name = "fabolousZoneId")
    private Long fabolousZone;
}
