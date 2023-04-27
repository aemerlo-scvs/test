package com.scfg.core.adapter.persistence.oldDisbursementsCreditLine;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.models.AgencyJpaEntity;
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
@Table(name = HelpersConstants.TABLE_OLD_MONTHLY_DISBURSEMENT)
@Data // change for getter and setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class OldMonthlyDisbursementCreditLineJpaEntity extends BaseJpaEntity {

    @Column(name = "userManager")
    private String userManager;

    @Column(name = "policyNumber")
    private String policyNumber;

    @Column(name = "contractor")
    private String contractor;

    @Column(name = "portfolio")
    private String portfolio;

    /*private String clientFullName;

    private String clienCi;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateBirth;*/

    /*private Long requestNumber;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate requestDate;*/

    @Column(name = "state")
    private String state;

    @Column(name = "acceptance")
    private String acceptance;

    @Column(name = "extraPremiumRate")
    private Double extraPremiumRate; // only regulated

    //private String currencyType;

    @Column(name = "requestAmount")
    private Double requestAmount; // Bs

    @Column(name = "accumulatedAmount")
    private Double accumulatedAmount; // Bs

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "companyPositionDate")
    private LocalDate companyPositionDate; // Only old disbursements

    //private String regional;
    //private String agency;

    @Column(name = "requestAttached")
    private String requestAttached;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "userManager")
    private LocalDate djsDate;*/

    private String certificateAttached;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "certificatedDate")
    private LocalDate certificatedDate; // emision DJS


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

    // For create
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "mortgageReliefItemId", updatable = false)
    @Where(clause = FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE)
    @JsonBackReference
    private MortgageReliefItemJpaEntity mortgageReliefItem;


}
