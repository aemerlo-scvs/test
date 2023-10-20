package com.scfg.core.adapter.persistence.CommercialManagementView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Immutable
@Table(name = "vv_virh_commercialManagementView")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class CommercialManagementViewJpaEntity {

    @Id
    @Column(name = "policyId")
    private Long policyId;

    @Column(name = "numberPolicy")
    private String numberPolicy;

    @Column(name = "productName")
    private String productName;

    @Column(name = "productInitials")
    private String productInitials;

    @Column(name = "insured")
    private String insured;

    @Column(name = "identificationNumber")
    private String identificationNumber;

    @Column(name = "policyStatus")
    private String policyStatus;

    @Column(name = "managementStatus")
    private String managementStatus;

    @Column(name = "managementSubStatus")
    private String managementSubStatus;

    @Column(name = "managementStatusIdc")
    private Integer managementStatusIdc;

    @Column(name = "managementSubStatusIdc")
    private Integer managementSubStatusIdc;

    @Column(name = "userName")
    private String userName;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "coverages")
    private String coverages;

    @Column(name = "number")
    private String number; // TODO

    @Column(name = "email")
    private String email;

    @Column(name = "planId")
    private Long planId;

    @Column(name = "planName")
    private String planName;

    @Column(name = "dateDifference")
    private Integer dateDifference;

    @Column(name = "endOfCoverage")
    private Date endOfCoverage;

    @Column(name = "issuanceDate")
    private Date issuanceDate;

    @Column(name = "fromDate")
    private Date fromDate;

    @Column(name = "code")
    private String code;

    @Column(name = "URL")
    private String URL;

    @Column(name = "commercialManagementId")
    private Long commercialManagementId;
}
