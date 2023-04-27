package com.scfg.core.adapter.persistence.insurancePolicyHolder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = HelpersConstants.TABLE_INSURANCE_POLICY_HOLDER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class InsurancePolicyHolderJpaEntity extends BaseJpaEntity {

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "foundationDAte")
    private LocalDate foundationDAte;

    @Column(name = "organizationName")
    private String organizationName;


    // Relationship foreign keys
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "activityTypeId", nullable = false)
    @JsonBackReference
    private ClassifierJpaEntity activityType;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationTypeId", nullable = false)
    @JsonBackReference
    private ClassifierJpaEntity organizationType;


    // Relationship contains object
    @OneToMany(mappedBy = "insurancePolicyHolder",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<MortgageReliefItemJpaEntity> mortgageReliefItems;

}
