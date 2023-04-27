package com.scfg.core.adapter.persistence.subscriptionTracking;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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
import java.time.LocalDate;

import static com.scfg.core.common.util.HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE;

@Entity
@Table(name = HelpersConstants.TABLE_SUBSCRIPTION_TRACKING)
@Data // change for getter and setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class SubscriptionTrackingJpaEntity extends BaseJpaEntity {

    private Long correlativeControl;

    private String item;

    private Long operationNumber;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateReceptionRequirementsDjs;*/

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate registrationDateDjs;

    //private String statementNumber; for insurance relation

    /*private String insured;

    private String gender;

    private String documentNumber;

    private String extension;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;*/

    //private String currency;

    //private Double requestedAmount;

    //private Double accumulatedAmoun;// original in USD

    private String requirements;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate complianceDateRequeriments;*/

    private String state; // comparing for request

    private String reason;

    private String additionalComments;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate bankPronouncementDate;*/

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate disbursementDate;*/

    private Integer validateTimeDjs;

    private String exclusionLetter; // carta exclusion

    private String risk;

    // value for comparation
    private Double rateExtrapremium;

    private String extraPremiumReason;

    private String coverageGranted;

    private String coverageDetail;

    private String examPerformed;

    //private String office;

    //private String branchOffice;

    //private String manager;

    private String requirementType;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate reinsuranceShipmentDate;

    private String operationType; // for subscription type

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate reinsuranceResponseDate;

    private Integer responseTimeReinsurance;

    private Double insuredCapital;

    private String level;

    private Integer creditTermMonths;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate broadcastDate;

    private String comments;

    private String process;


    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate schedulingDate;*/

    //private String portfolio;


    // Relationship
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "regionalIdc", updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity regional;


    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", nullable = false, updatable = false)
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


    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "insuranceRequestId", updatable = false)
    @JsonBackReference
    private InsuranceRequestJpaEntity insuranceRequest;


    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyIdc", updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity currency;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "coverageIdc", updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity coverage;


    // For create
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "mortgageReliefItemId", updatable = false)
    @Where(clause = FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE)
    @JsonBackReference
    private MortgageReliefItemJpaEntity mortgageReliefItem;


}
