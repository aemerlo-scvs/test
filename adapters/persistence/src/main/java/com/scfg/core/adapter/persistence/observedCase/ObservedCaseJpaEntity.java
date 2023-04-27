package com.scfg.core.adapter.persistence.observedCase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.consolidatedObservedCase.ConsolidatedObservedCaseJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = HelpersConstants.TABLE_OBSERVED_CASE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ObservedCaseJpaEntity extends BaseJpaEntity {

    @Column(name = "accumulated", nullable = false)
    private Double accumulated;

    @Column(name = "currentMonthComments", nullable = false)
    private String currentMonthComments;

    @Column(name = "currentMonthDisbursement", nullable = false)
    private Double currentMonthDisbursement;

    @Column(name = "previousMonthDisbursement", nullable = false)
    private Double previousMonthDisbursement;

    // Relationships
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", nullable = false, updatable = false)
    @JsonBackReference
    private ClientJpaEntity client;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "creditOperationId", nullable = false, updatable = false)
    @JsonBackReference
    private CreditOperationJpaEntity creditOperation;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "mortgageReliefItemId", nullable = false, updatable = false)
    @JsonBackReference
    private MortgageReliefItemJpaEntity mortgageReliefItem;


    // Incoming relationship
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "observedCase")
    @JsonManagedReference
    private List<ConsolidatedObservedCaseJpaEntity> consolidatedObservedCases;
}
