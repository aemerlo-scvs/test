package com.scfg.core.adapter.persistence.naturalPerson;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.person.PersonJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "NaturalPerson")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class NaturalPersonJpaEntity extends BaseJpaEntity {

    @Column(name = "clientCode")
    private Integer clientCode;

    @Column(name = "clientEventual")
    private Integer clientEventual;

    @Column(name = "clientType")
    private Integer clientType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "lastName", length = 100)
    private String lastName;

    @Column(name = "motherLastName", length = 100)
    private String motherLastName;

    @Column(name = "marriedLastName", length = 100)
    private String marriedLastName;

    @Column(name = "maritalStatusIdc")
    private Integer maritalStatusIdc;

    @Column(name = "documentType")
    private Integer documentType;

    @Column(name = "identificationNumber", length = 15)
    private String identificationNumber;

    @Column(name = "complement", length = 10)
    private String complement;

    @Column(name = "extIdc")
    private Integer extIdc;

    @Column(name = "birthDate")
    private LocalDateTime birthDate;

    @Column(name = "genderIdc")
    private Integer genderIdc;

    @Column(name = "profession", length = 200)
    private String profession;

    @Column(name = "workPlace", length = 200)
    private String workPlace;

    @Column(name = "workTypeIdc")
    private Integer workTypeIdc;

    @Column(name = "position", length = 200)
    private String position;

    @Column(name = "entryDate")
    private LocalDateTime entryDate;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "nit")
    private Long nit;

    @Column(name = "internalClientCode", insertable = false)
    private Long internalClientCode;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "personId")
    @JsonBackReference
    private PersonJpaEntity person;
}
