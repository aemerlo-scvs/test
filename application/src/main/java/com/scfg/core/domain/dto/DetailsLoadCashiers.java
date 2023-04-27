package com.scfg.core.domain.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter

public class DetailsLoadCashiers {
//    public static final String PROP_ANSWER = "answer";
//    public static final String PROP_CNT = "cnt";
//    public static final String PROP_ANSWER = "answer";
//    public static final String PROP_CNT = "cnt";
    private Date courtDate;
    private Long createdBy;
    private LocalDate createdAt;
    private Integer cant;

    public DetailsLoadCashiers(Date courtDate, Long createdBy, LocalDate createdAt, Integer cant) {
        this.courtDate = courtDate;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.cant = cant;
    }
}
