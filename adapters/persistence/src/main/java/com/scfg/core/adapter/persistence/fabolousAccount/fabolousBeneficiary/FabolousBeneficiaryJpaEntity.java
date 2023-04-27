package com.scfg.core.adapter.persistence.fabolousAccount.fabolousBeneficiary;

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
@Table(name = HelpersConstants.TABLE_FBS_BENEFICIARY)
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class FabolousBeneficiaryJpaEntity extends BaseJpaEntity {

    private String fullName;

    private double percentage;

    private String relationshipIdc;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousClientId", updatable = false, insertable = false)
//    @JsonBackReference
//    private FabolousClientJpaEntity fabolousClient;

//    @Column(name = "fabolousClientId")
//    private Long fabolousClient;

    //    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousInsuranceId", updatable = false, insertable = false)
//    @JsonBackReference
//    private FabolousInsuranceJpaEntity fabolousInsurance;

    @Column(name = "fabolousInsurancetId")
    private Long fabolousInsurance;
}
