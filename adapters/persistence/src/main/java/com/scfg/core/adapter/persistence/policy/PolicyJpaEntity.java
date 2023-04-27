package com.scfg.core.adapter.persistence.policy;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.generalRequest.GeneralRequestJpaEntity;
import com.scfg.core.adapter.persistence.person.PersonJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "Policy")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PolicyJpaEntity extends BaseJpaEntity {

    @Column(name = "generalRequestId")
    private Long generalRequestId;

    @Column(name = "currencyTypeIdc")
    private Integer currencyTypeIdc;

    @Column(name = "exchangeRate")
    private Integer exchangeRate;

    @Column(name = "correlativeNumber")
    private Long correlativeNumber;

    @Column(name = "numberPolicy")
    private String numberPolicy;

    @Column(name = "issuanceDate")
    private Date issuanceDate;

    @Column(name = "fromDate")
    private Date fromDate;

    @Column(name = "toDate")
    private Date toDate;

    @Column(name = "totalPremium")
    private Double totalPremium;

    @Column(name = "additionalPremium")
    private Double additionalPremium;

    @Column(name = "netPremium")
    private Double netPremium;

    @Column(name = "riskPremium")
    private Double riskPremium;

    @Column(name = "aps")
    private Double aps;

    @Column(name = "fpa")
    private Double fpa;

    @Column(name = "iva")
    private Double iva;

    @Column(name = "it")
    private Double it;

    @Column(name = "intermediaryCommission")
    private Double intermediaryCommission;

    @Column(name = "intermediaryCommissionPercentage")
    private Double intermediaryCommissionPercentage;

    @Column(name = "collectionServiceCommission")
    private Double collectionServiceCommission;//fk recursion

    @Column(name = "collectionServiceCommissionPercentage")
    private Double collectionServiceCommissionPercentage;

    @Column(name = "insuredCapital")
    private Double insuredCapital;

    @Column(name = "policyStatusIdc", nullable = false)
    private Integer policyStatusIdc;

    @Column(name = "branchOfficeId")
    private Long branchOfficeId;

    @Column(name = "bfsAgencyId")
    private Long agencyId;

    @Column(name = "renewal", nullable = false)
    private Integer renewal;

    @Column(name = "assignedExecutiveId")
    private Long assignedExecutiveId;

    @Column(name = "intermediaryTypeIdc")
    private Integer intermediaryTypeIdc;

    @Column(name = "brokerId")
    private Long brokerId;

    @Column(name = "bfsAgentId")
    private Long agentId;

    @Column(name = "productId")
    private Long productId;

}
