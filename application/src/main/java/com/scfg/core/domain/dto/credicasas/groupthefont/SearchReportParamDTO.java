package com.scfg.core.domain.dto.credicasas.groupthefont;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Null;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SearchReportParamDTO {
    private Long groupId;
    private Long takerId;
    private Long branchId;
    private Long productId;
    @Null
    private Long planId;
    @Null
    private int requestStatusIdc;
    @Null
    private Date fromDate;
    @Null
    private Date toDate;
}
