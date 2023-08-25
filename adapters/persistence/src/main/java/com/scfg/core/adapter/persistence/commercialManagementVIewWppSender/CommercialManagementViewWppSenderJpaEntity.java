package com.scfg.core.adapter.persistence.commercialManagementVIewWppSender;

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
import java.util.UUID;

@Entity
@Immutable
@Table(name = "vv_virh_priority_client_sender")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CommercialManagementViewWppSenderJpaEntity {
    @Id
    @Column(name = "commercialManagementId")
    private Long commercialManagementId;

    @Column(name = "generalRequestId")
    private Long generalRequestId;

    @Column(name = "dateDifference")
    private Integer dateDifference;

    @Column(name = "endOfCoverage")
    private Date endOfCoverage;

    @Column(name = "prioritySender")
    private Integer prioritySender;

    @Column(name = "managementStatusIdc")
    private Integer managementStatusIdc;

    @Column(name = "managementStatus")
    private String managementStatus;

    @Column(name = "managementSubStatusIdc")
    private Integer managementSubStatusIdc;

    @Column(name = "managementSubStatus")
    private String managementSubStatus;

    @Column(name = "productName")
    private String productName;

    @Column(name = "insured")
    private String insured;

    @Column(name = "number")
    private String number;

    @Column(name = "numberPolicy")
    private String numberPolicy;

    @Column(name = "uniqueCode")
    private UUID uniqueCode;
}
