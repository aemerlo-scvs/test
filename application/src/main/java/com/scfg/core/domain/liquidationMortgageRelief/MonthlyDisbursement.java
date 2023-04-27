package com.scfg.core.domain.liquidationMortgageRelief;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MonthlyDisbursement {


    private Long creditOperationNumber;

    private String clientDocumentNumber;

    private Integer caseStatus;


    // not using
    private String documentType;

    private String place;

    private Double disbursedAmount; //

    private Double insuredValue;

    private LocalDate expirationDate;

    private String borrowRole;

    private String borrowCoverage;


    private String period;

    private int creditTermDays;

    // prima
    private Double premiumAmount;

    private Double ratePremium;

    // extra prima
    private Double extraPremium;

    private LocalDate paidFrom;

    private LocalDate paidUp;

    // Outgoing Relationship
    /*private ClientJpaEntity client;

    private CreditOperationJpaEntity creditOperation;

    private AgencyJpaEntity agency;

    private ClassifierJpaEntity creditType;

    private ClassifierJpaEntity coverage;

    private ClassifierJpaEntity currency;

    private MortgageReliefItemJpaEntity mortgageReliefItem;*/

}
