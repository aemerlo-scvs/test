package com.scfg.core.adapter.persistence.user;

import com.scfg.core.adapter.persistence.BaseJpaEntity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scfg.core.adapter.persistence.mortgageReliefitem.MortgageReliefItemJpaEntity;

import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class UserJpaEntity extends BaseJpaEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surName", nullable = false)
    private String surName;

    @Column(name = "genderIdc", nullable = false)
    private Long genderIdc;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "secretQuestion")
    private String secretQuestion;

    @Column(name = "secretAnswer")
    private String secretAnswer;

    @Column(name = "regionalIdc")
    private Long regionalIdc;

    @Column(name = "officeIdc")
    private Long officeIdc;

    @Column(name = "companyIdc")
    private Long companyIdc;

    @Column(name = "token")
    private String token;

    @Column(name = "roleId", nullable = false)
    private Long roleId;

    @Column(name = "bfsUserCode", nullable = false)
    private Long bfsUserCode;

    @Column(name = "bfsAgencyCode", nullable = false)
    private Long bfsAgencyCode;


    // Relationship

    /*@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @JsonIgnoreProperties("users")
    private List<MortgageReliefItemJpaEntity> mortgageReliefItems;*/

    //@OneToMany
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<MortgageReliefItemJpaEntity> mortgageReliefItems;


}
