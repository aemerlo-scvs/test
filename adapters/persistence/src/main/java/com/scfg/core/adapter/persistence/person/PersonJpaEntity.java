package com.scfg.core.adapter.persistence.person;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.juridicalPerson.JuridicalPersonJpaEntity;
import com.scfg.core.adapter.persistence.naturalPerson.NaturalPersonJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "Person")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PersonJpaEntity extends BaseJpaEntity {

    @Column(name = "nationalityIdc")
    private Integer nationalityIdc;

    @Column(name = "residenceIdc")
    private Integer residenceIdc;

    @Column(name = "activityIdc")
    private Integer activityIdc;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "holder")
    private Integer holder;

    @Column(name = "insured")
    private Integer insured;

    @Column(name = "assignedGroupIdc")
    private Integer assignedGroupIdc;
}
