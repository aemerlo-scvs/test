package com.scfg.core.adapter.persistence.request;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.creditOperation.CreditOperationJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.common.CreditOperation;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = HelpersConstants.TABLE_INSURANCE_REQUEST)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class InsuranceRequestJpaEntity extends BaseJpaEntity {

    @Column(name = "requestNumber", nullable = false)
    private String requestNumber;

    // Fecha Generacion de solicitud de seguro
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "insuranceRequestDate")
    private LocalDate insuranceRequestDate;

    // Fecha Agendamiento
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "schedulingDate")
    private LocalDate schedulingDate;

    // Fecha de cumplimiento de requisitos
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "fulfillmentRequirementsDate")
    private LocalDate fulfillmentRequirementsDate;

    // updated for refresh massive importation
    @Column(name = "requestedAmount")
    private Double requestedAmount;

    @Column(name = "accumulatedAmount")
    private Double accumulatedAmount;

    // Fecha de desembolso
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "disbursementDate")
    private LocalDate disbursementDate; // for credit operat

    // Fecha de exclusion
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "exclusionDate")
    private LocalDate exclusionDate;

    // Fecha de inclusion
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "inclusionDate")
    private LocalDate inclusionDate;

    // Fecha de llenado DJS
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "djsFillDate")
    private LocalDate djsFillDate; // or djs for old disbursements

    // Fecha de pronunciamiento compañia
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "pronouncementDate")
    private LocalDate pronouncementDate;

    // Fecha de pronunciamiento del banco
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "bankPronouncementDate")
    private LocalDate bankPronouncementDate;

    // Fecha de recepcion DJS
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "receptionDate")
    private LocalDate djsReceptionDate;

    // Fecha de aceptacion
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "acceptanceDate")
    private LocalDate acceptanceDate;

    // Relationships
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "requestStatusId")
    @JsonBackReference
    private ClassifierJpaEntity requestStatus;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId")
    @JsonBackReference
    private ClassifierJpaEntity currency;

    // income relationship
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "insuranceRequest")
    @JsonManagedReference
    private List<CreditOperationJpaEntity> creditOperations;

    // custom methods
   public void refrehsRelationship(){
       this.setRequestStatus(null);
       this.setCurrency(null);
   }


}
