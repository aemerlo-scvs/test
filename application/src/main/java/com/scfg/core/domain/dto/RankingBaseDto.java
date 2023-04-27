package com.scfg.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RankingBaseDto {
    private String reporte;
    private Date corteMes;
    private Integer mes;
    private Integer years;
    private String branchOffice;
    private Integer objectives;
    private Integer sales;
    private Integer amountSales;

}
