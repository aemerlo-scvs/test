package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ObservedCase extends BaseDomain {

    private double accumulated;

    private String currentMonthComments;

    private double currentMonthDisbursement;

    private double previousMonthDisbursement;

    private Long clientId;

    private Long creditOperationId;

    private Long mortgageReliefItemId;

    // Relationships
    private Client client;

    private CreditOperation creditOperation;

    // Falta crear este modelo de dominio
    // private MortageReliefItemJpaEntity mortgageReliefItem;
}
