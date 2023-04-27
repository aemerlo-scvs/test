package com.scfg.core.adapter.persistence.subscriptionReport;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = HelpersConstants.TABLE_SUBSCRIPTION_REPORT)
@Data // change for getter and setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SubscriptionReportJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apolicyType", nullable = false)
    private String apolicyType;

    @Column(name = "bpolicyCode", nullable = false)
    private String bpolicyCode;

    @Column(name = "cdjsFillDate", nullable = false)
    private String cdjsFillDate;

    @Column(name = "doperationNumber", nullable = false)
    private String doperationNumber;

    @Column(name = "erequestNumber", nullable = false)
    private String erequestNumber;

    @Column(name = "finsured", nullable = false)
    private String finsured;

    @Column(name = "ggender", nullable = false)
    private String ggender;

    @Column(name = "hdocumentNumber", nullable = false)
    private String hdocumentNumber;

    @Column(name = "iextension", nullable = false)
    private String iextension;

    @Column(name = "jbirthDate", nullable = false)
    private String jbirthDate;

    @Column(name = "kage", nullable = false)
    private Integer kage;

    @Column(name = "lcurrency", nullable = false)
    private String lcurrency;

    @Column(name = "mrequestAmountBs", nullable = false)
    private Float mrequestAmountBs;

    @Column(name = "nsubscriptionStatus", nullable = false)
    private String nsubscriptionStatus;

    @Column(name = "oextraPremiumRate", nullable = false)
    private Float oextraPremiumRate;

    @Column(name = "pgrantedCoverage", nullable = false)
    private String pgrantedCoverage;

    @Column(name = "qpronouncementDate", nullable = false)
    private String qpronouncementDate;

    @Column(name = "rcoverage", nullable = false)
    private String rcoverage;
}
