package com.scfg.core.adapter.persistence.commecialManagement;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CommercialManagement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class CommercialManagementJpaEntity extends BaseJpaEntity {
    @Column(name = "comercialManagementId")
    private UUID commercialManagementId;

    @Column(name = "policyId")
    private Long policyId;

    @Column(name = "endOfCoverage")
    private Date endOfCoverage;

    @Column(name = "managementStatusIdc")
    private Integer managementStatusIdc;

    @Column(name = "managementSubStatusIdc")
    private Integer managementSubStatusIdc;

    @Column(name = "assignedUserId")
    private Long assignedUserId;

    @Column(name = "messageSentDate")
    private Date messageSentDate;

    @Column(name = "linkEntries")
    private Integer linkEntries;

    @Column(name = "firstEntryDate")
    private Date firstEntryDate;

    @Column(name = "lastEntryDate")
    private Date lastEntryDate;

    @Column(name = "renewalPlanId")
    private Integer renewalPlanId;
}
