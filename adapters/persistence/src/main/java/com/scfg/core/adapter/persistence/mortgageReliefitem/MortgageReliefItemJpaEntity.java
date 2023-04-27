package com.scfg.core.adapter.persistence.mortgageReliefitem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.brokerSettlementCalculations.BrokerSettlementCalculationsJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.insurancePolicyHolder.InsurancePolicyHolderJpaEntity;
import com.scfg.core.adapter.persistence.lastCasesObserved.LastObservedCaseJpaEntity;
import com.scfg.core.adapter.persistence.manualCertificate.ManualCertificateJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.adapter.persistence.observedCase.ObservedCaseJpaEntity;
import com.scfg.core.adapter.persistence.oldDisbursementsCreditLine.OldMonthlyDisbursementCreditLineJpaEntity;
import com.scfg.core.adapter.persistence.pastMonthlyDisbursements.PastMonthlyDisbursementJpaEntity;
import com.scfg.core.adapter.persistence.sinister.SinisterJpaEntity;
import com.scfg.core.adapter.persistence.subscriptionTracking.SubscriptionTrackingJpaEntity;
import com.scfg.core.adapter.persistence.user.UserJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "rollbackRelatedEntities",
                procedureName = HelpersConstants.CURRENT_SCHEMA_DB_NAME+".sp_rollbackRelatedEntities",
                parameters = {
                        @StoredProcedureParameter(
                                mode = ParameterMode.IN,
                                name = "rollbackCreditOperation",
                                type = Long.class),
                        @StoredProcedureParameter(
                                mode = ParameterMode.IN,
                                name = "rollbackInsuranceRequest",
                                type = Long.class),
                        @StoredProcedureParameter(
                                mode = ParameterMode.IN,
                                name = "rollbackClient",
                                type = Long.class),
                        @StoredProcedureParameter(
                                mode = ParameterMode.IN,
                                name = "rollbackMortgageReliefItem",
                                type = Long.class)
                }
        )
})
@Entity
@Table(name = HelpersConstants.TABLE_MORTGAGE_RELIEF_ITEM)
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class MortgageReliefItemJpaEntity extends BaseJpaEntity {

    @Column(name = "activeRecord")
    private Integer activeRecord;

    // Relationship Foreign Key
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "monthIdc", nullable = false, updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity loadMonth;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "yearIdc", nullable = false, updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity loadYear;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "reportTypeIdc", nullable = false, updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity reportType;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "policyTypeIdc", nullable = false, updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity policyType;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "usersId", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonBackReference
    private UserJpaEntity user;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "insurancePolicyHolderIdc", nullable = false, updatable = false)
    @JsonBackReference
    private InsurancePolicyHolderJpaEntity insurancePolicyHolder;


    // Relationship contains objects
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<BrokerSettlementCalculationsJpaEntity> brokerSettlementCalculations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<LastObservedCaseJpaEntity> lastObservedCase;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<ManualCertificateJpaEntity> manualCertificate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<ObservedCaseJpaEntity> observedCase;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<MonthlyDisbursementJpaEntity> monthlyDisbursement;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<SubscriptionTrackingJpaEntity> subscriptionTracking;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<OldMonthlyDisbursementCreditLineJpaEntity> oldMonthlyDisbursementCreditLine;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<SinisterJpaEntity> sinister;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "mortgageReliefItem")
    @JsonManagedReference
    private List<PastMonthlyDisbursementJpaEntity> pastMonthlyDisbursement;


// Contains
    /*@ManyToMany(fetch = FetchType.LAZY, targetEntity = UserJpaEntity.class)
    @JoinTable(name = HelpersConstants.TABLE_INTEMERDIARY_MORTGAGE_RELIEF_ITEM_USER,
            joinColumns = { @JoinColumn(name = "mortgageReliefItemId") },
            inverseJoinColumns = { @JoinColumn(name = "userId") })
    @JsonIgnoreProperties("mortgageReliefItems")
    private List<UserJpaEntity> users;*/

    @PrePersist
    public void prePersist() {
        activeRecord = 1;
    }


}
