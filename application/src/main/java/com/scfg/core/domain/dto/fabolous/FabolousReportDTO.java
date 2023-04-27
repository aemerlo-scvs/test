package com.scfg.core.domain.dto.fabolous;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class FabolousReportDTO implements Serializable {
    private Date dateFrom;
    private Date dateTo;

//    public FabolousReportDTO(Date dateFrom, Date dateTo) {
//        this.dateFrom = dateFrom;
//        this.dateTo = dateTo;
//    }
}
