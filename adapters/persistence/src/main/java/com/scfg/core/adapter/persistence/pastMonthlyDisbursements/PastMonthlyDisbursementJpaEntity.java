package com.scfg.core.adapter.persistence.pastMonthlyDisbursements;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.monthlyDisbursements.MonthlyDisbursementJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = HelpersConstants.TABLE_PAST_MONTHLY_DISBURSEMENT)
@Data // change for getter and setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PastMonthlyDisbursementJpaEntity extends BaseJpaEntity {

    private Long creditOperationNumber;

    private String clientNames;

    private String clientLastname;

    private String clientMotherLastname;

    private String clientMarriedLastname;



    private String documentNumber;

    private String duplicateCopy;

    private String extension;

    private String documentType;

    private String place;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate disbursementDate;

    private Double disbursedAmount; //

    private Double insuredValue;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expirationDate;

    //private int currencyType;

    //private String creditType;

    private String borrowRole;

    private String borrowCoverage;


    //private String gender;

    private String period;

    //private String creditLine;

    private int creditTermDays;

    // prima
    private Double premiumAmount;
    private Double ratePremium;

    // extra prima
    private Double extraPremium;


    //private String nationality;

    //private String agency;


    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate paidFrom;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate paidUp;

    // Outgoing Relationship
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", updatable = false)
    @JsonBackReference
    private ClientJpaEntity client;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "creditOperationId", updatable = false)
    @JsonBackReference
    private CreditOperationJpaEntity creditOperation;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "agencyId", referencedColumnName = "agency_id", updatable = false)
    @JsonBackReference
    private AgencyJpaEntity agency;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "creditTypeIdc", updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity creditType;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "coverageIdc", updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity coverage;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyIdc", updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity currency;


    // for create
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "mortgageReliefItemId", updatable = false)
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
