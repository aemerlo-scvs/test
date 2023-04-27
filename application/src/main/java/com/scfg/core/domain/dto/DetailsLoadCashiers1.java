package com.scfg.core.domain.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;



public interface DetailsLoadCashiers1 {
//    public static final String PROP_ANSWER = "answer";
//    public static final String PROP_CNT = "cnt";
//    public static final String PROP_ANSWER = "answer";
//    public static final String PROP_CNT = "cnt";
    Date getCourtDate();
    Long getCreatedBy();
    LocalDate getCreatedAt();
    Integer getCant();


}
