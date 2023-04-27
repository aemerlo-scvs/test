package com.scfg.core.adapter.persistence.fabolousAccount.fabolousInsurance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = HelpersConstants.TABLE_FBS_INSURANCE)
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
//@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
public class FabolousInsuranceJpaEntity extends BaseJpaEntity {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "startDate")
    private LocalDate startDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "finishDate")
    private LocalDate finishDate;

    private double insuredCapital;

    private double premium;

    private double entryAge;

    private Long accountNumber;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousClientId", updatable = false, insertable = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    private FabolousClientJpaEntity fabolousClient;

    @Column(name = "fabolousClientId")
    private Long fabolousClient;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousUploadReportId")
////    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    private FabolousUploadReportJpaEntity fabolousUploadReport;

    @Column(name = "fabolousUploadReportId")
    private Long fabolousUploadReportId;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "fabolousManagerAgencyId", updatable = false, insertable = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    private FabolousManagerAgencyJpaEntity fabolousManagerAgency;

    @Column(name = "fabolousManagerAgencyId")
    private Long fabolousManagerAgency;
}
