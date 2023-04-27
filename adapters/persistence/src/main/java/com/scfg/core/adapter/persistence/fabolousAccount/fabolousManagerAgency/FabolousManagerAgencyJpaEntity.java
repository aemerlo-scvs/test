package com.scfg.core.adapter.persistence.fabolousAccount.fabolousManagerAgency;

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
import java.util.Date;

@Entity
@Table(name = HelpersConstants.TABLE_FBS_MANAGERAGENCY)
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class FabolousManagerAgencyJpaEntity extends BaseJpaEntity {

    private int managerStatus;

    private int agencyStatus;

//    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousAgencyId")
//    @JsonBackReference
//    private FabolousAgencyJpaEntity fabolousAgency;

    @Column(name = "fabolousAgencyId")
    private Long fabolousAgency;

//    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousManagerId")
//    @JsonBackReference
//    private FabolousManagerJpaEntity fabolousManager;

    @Column(name = "fabolousManagerId")
    private Long fabolousManager;

    @Column(name = "exclusionDate", nullable = true)
    private Date exclusionDate;
}
