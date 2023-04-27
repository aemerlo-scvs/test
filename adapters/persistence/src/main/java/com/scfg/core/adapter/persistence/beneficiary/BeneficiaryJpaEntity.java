package com.scfg.core.adapter.persistence.beneficiary;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.policy.PolicyJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.domain.Policy;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "Beneficiary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class BeneficiaryJpaEntity extends BaseJpaEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "motherLastName")
    private String motherLastName;

    @Column(name = "marriedLastName")
    private String marriedLastName;

    @Column(name = "identification")
    private String identification;

    @Column(name = "birthDate")
    private LocalDateTime birthDate;

    @Column(name = "percentage")
    private Integer percentage;

    @Column(name = "relationShipIdc")
    private Integer relationShipIdc;

    // @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policyId")
    @JsonBackReference
    private PolicyJpaEntity policy;

    @Column(name = "isUnderAge")
    private boolean isUnderAge;
    @Column(name = "representativeLegalName")
    private String representativeLegalName;
    @Column(name = "legalIdentification")
    private String legalIdentification;
    @Column(name = "legalExt")
    private Integer legalExt;

    @Column(name = "policyItemId")
    private Long policyItemId;

}
