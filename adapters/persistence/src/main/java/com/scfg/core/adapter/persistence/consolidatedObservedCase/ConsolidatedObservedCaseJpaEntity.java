package com.scfg.core.adapter.persistence.consolidatedObservedCase;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.observedCase.ObservedCaseJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

import static com.scfg.core.common.util.HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE;

@Entity
@Table(name = HelpersConstants.TABLE_CONSOLIDATED_OBSERVED_CASE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ConsolidatedObservedCaseJpaEntity extends BaseJpaEntity {

    @Column(name = "insuredCapital", nullable = false)
    private Double insuredCapital;

    @Column(name = "brokerComments", nullable = false)
    private String brokerComments;

    @Column(name = "condition", nullable = false)
    private String condition;

    // {DEPURAR = 0, RECONSIDERAR = 1}
    @Column(name = "finalStatus", nullable = false)
    private Integer finalStatus;

    @Column(name = "lastAcceptanceDate", nullable = false)
    private LocalDate lastAcceptanceDate;

    // Relationships
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "insuranceRequestId", nullable = false, updatable = false)
    @JsonBackReference
    private InsuranceRequestJpaEntity insuranceRequest;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyIdc", nullable = false, updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity currency;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "observedCaseId", nullable = false, updatable = false)
    @Where(clause = FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE)
    @JsonBackReference
    private ObservedCaseJpaEntity observedCase;
}
