package com.scfg.core.adapter.persistence.lastCasesObserved;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.client.ClientJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
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
@Table(name = HelpersConstants.TABLE_LAST_OBSERVED_CASE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class LastObservedCaseJpaEntity extends BaseJpaEntity {

    @Column(name = "balance", nullable = false)
    private Double balance;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "disbursementDate", nullable = false)
    private LocalDate disbursementDate;

    @Column(name = "exludedCapital", nullable = false)
    private Double exludedCapital;


    // Incoming Relationship
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "creditOperationId", nullable = false, updatable = false)
    @JsonBackReference
    private CreditOperationJpaEntity creditOperation;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", nullable = false)
    @JsonBackReference
    private ClientJpaEntity client;


    @ManyToOne(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
    @JoinColumn(name = "observationTypeIdc", nullable = false, updatable = false)
    @JsonBackReference
    private ClassifierJpaEntity observationType;


    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "mortgageReliefItemId", updatable = false)
    @Where(clause = FILTER_ACTIVE_RECORDS_FOR_OVERVWRITE)
    @JsonBackReference
    private MortgageReliefItemJpaEntity mortgageReliefItem;


    // Outgoing relationship
    //private List<Clas>




}
