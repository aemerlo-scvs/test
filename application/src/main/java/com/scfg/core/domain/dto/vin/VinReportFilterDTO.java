package com.scfg.core.domain.dto.vin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class VinReportFilterDTO {
    private Date fromDate;
    private Date toDate;
    private Integer policyStatusIdc;
    private Integer regionalIdc;
}
