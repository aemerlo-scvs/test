package com.scfg.core.domain.dto;

import com.scfg.core.common.util.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
public class RankingAgenciasDto extends RankingBaseDto {
private String zonas;
private String agencies;
private Integer codAgency;
private Float averageSales;
private Integer amountAgency;
public RankingAgenciasDto(Object []objects){
    this.setReporte((String)objects[0]);
    this.setCorteMes(((Date)objects[1]));
    this.setMes((Integer) objects[2]);
    this.setYears((Integer) objects[3]);
    this.codAgency =((BigInteger)objects[4]).intValue();
    this.agencies =(String) objects[5];
    this.setBranchOffice((String) objects[6]);
    this.zonas=(String) objects[7];
    this.amountAgency=(Integer)objects[8];
    this.setSales((((Integer) objects[9]).intValue()));
    this.setAmountSales((((Integer) objects[10]).intValue()));
    this.setObjectives(((Integer) objects[11]).intValue());
    this.setAverageSales((((Double) objects[12]).floatValue()));
}
}
