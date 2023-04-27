package com.scfg.core.adapter.persistence.brokerSettlementCalculations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import static com.scfg.core.common.util.HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE;

@Entity
@Table(name = HelpersConstants.TABLE_BROKER_SETTLEMENT_CALCULATIONS)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class BrokerSettlementCalculationsJpaEntity extends BaseJpaEntity {


    // Provisional
    @Column(name = "totalInsuredCapital", nullable = false)
    private String totalInsuredCapital;

    @Column(name = "premiumAmount", nullable = false)
    private Double premiumAmount;

    @Column(name = "extraPremiumAmount" )
    private Double extraPremiumAmount;

    @Column(name = "totalPremiumAmount")
    private Double totalPremiumAmount;

    @Column(name = "premiumCompanyAmount")
    private Double premiumCompanyAmount;

    @Column(name = "commisionBankAmount", nullable = false)
    private Double commisionBankAmount;

    @Column(name = "totalInsureds", nullable = false)
    private Integer totalInsureds;

    // Relationship
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "insuredCoverageIdc", nullable = false, updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity insuredCoverage;


    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "mortgageReliefItemId", updatable = false)
    //@Where(clause = FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE)
    @JsonBackReference
    private MortgageReliefItemJpaEntity mortgageReliefItem;



}
