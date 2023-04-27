package com.scfg.core.adapter.persistence.creditOperation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = HelpersConstants.TABLE_CREDIT_OPERATION)
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class CreditOperationJpaEntity extends BaseJpaEntity {

    @Column(name = "operationNumber", nullable = false)
    private Long operationNumber;

    @Column(name = "disbursedAmount")
    private Double disbursedAmount; // Bs


    @Column(name = "creditLine")
    private Integer creditLine; // SI/NO

    @Column(name = "insuredValue")
    private Double insuredAmount; // Bs

    // reply value to Request Entity
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "disbursementDate")
    private LocalDate disbursementDate;


    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "expirationDate")
    private LocalDate expirationDate;

    @Column(name = "deadlineDays")
    private Integer deadlineDays; // limit credit in days

    // extrapremium
    @Column(name = "extraPremiumRate")
    private Double extraPremiumRate;

    @Column(name = "extraPremiumValue")
    private Double extraPremiumValue;

    // premium
    @Column(name = "premiumRate")
    private Double premiumRate;

    @Column(name = "premiumValue")
    private Double premiumValue;

    // Relationships
    //@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "currencyIdc")
    @JsonBackReference
    //@NotFound(action = NotFoundAction.IGNORE)
    private ClassifierJpaEntity currency;

    //@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @OneToOne(fetch = FetchType.LAZY/*, cascade = CascadeType.MERGE*/)
    @JoinColumn(name = "insuranceRequestId")
    @JsonBackReference
    //@NotFound(action = NotFoundAction.IGNORE)
    private InsuranceRequestJpaEntity insuranceRequest;

    // custom methods
    public void refrehsRelationship() {
        this.setCurrency(null);
        this.setInsuranceRequest(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CreditOperationJpaEntity that = (CreditOperationJpaEntity) o;
        return Objects.equals(operationNumber, that.operationNumber)
                && Objects.equals(disbursedAmount, that.disbursedAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), operationNumber, disbursedAmount);
    }
}
