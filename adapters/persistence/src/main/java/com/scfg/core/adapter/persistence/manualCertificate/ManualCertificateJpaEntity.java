package com.scfg.core.adapter.persistence.manualCertificate;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.models.ManagerJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import static com.scfg.core.common.util.HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE;

@Entity
@Table(name = HelpersConstants.TABLE_MANUAL_CERTIFICATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ManualCertificateJpaEntity extends BaseJpaEntity {

    /*@Column(name = "djsManualNumber")
    private Long djsManualNumber;*/

    private String policyType;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate registrationDateDjs;*/

    private Long creditOperationNumber; // optional

    //private String clientFullName;

    //private String clientCi;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateBirth;*/

    //private String gender;

    //private String nationality;

    private Double weight;

    private Double heightCm;

    //private String manager;

    //private String agency;

    //private Long currencyType;

    private Double requestedAmount;

    private Double accumulatedAmount;

    private Integer crediTermDays;

    private Integer daysMonthsYears;

    //private String creditType;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate acceptandeDate;*/

   // private Long certicateNumber;

   /* @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate disbusementDate;*/

    private Double insuredValue;

    //private Double insuredValueUsd;

    private Double ratePremium;

    //private String coverage;

    private Double rateExtrapremium;

    private Double premium;

    private Double rateExtrapremiumBank;

    private Double extraPremium;


    // Relationship

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "insuranceRequestId", updatable = false)
    @JsonBackReference
    private InsuranceRequestJpaEntity insuranceRequest;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", updatable = false)
    @JsonBackReference
    private ClientJpaEntity client;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "managerId", referencedColumnName = "manager_id", updatable = false)
    @JsonBackReference
    private ManagerJpaEntity manager;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "agencyId",referencedColumnName = "agency_id", updatable = false)
    @JsonBackReference
    private AgencyJpaEntity agency;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId",  updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity currency;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "coverageId", updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity coverage;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "creditTypeId", updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity creditType;

    // for create
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "mortgageReliefItemId", updatable = false)
    @Where(clause = FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE)
    @JsonBackReference
    private MortgageReliefItemJpaEntity mortgageReliefItem;

    // Incoming Relationship
    /*@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "creditOperationId", nullable = false, updatable = false)
    @JsonBackReference
    private CreditOperationJpaEntity creditOperation;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", nullable = false, updatable = false)
    @JsonBackReference
    private ClientJpaEntity client;


    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "observationTypeIdc", nullable = false, updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity observationType;


    */

}
