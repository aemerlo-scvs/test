package com.scfg.core.domain.dto;

import com.scfg.core.common.util.DateUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
@SuperBuilder
public class RankingZonasDto extends RankingBaseDto {
 private String zonas;
 private Integer amountZones;
 private Float averageSales;
 public RankingZonasDto(Object []objects){
  this.setReporte((String)objects[0]);
  //falta de la fecha
  this.setCorteMes(((Date)objects[1]));
  this.setMes((Integer) objects[2]);
  this.setYears((Integer) objects[3]);
  this.zonas=(String) objects[4];
  this.setBranchOffice((String) objects[5]);
  this.setAmountZones(((Integer) objects[6]).intValue());
  this.setSales((((Integer) objects[7]).intValue()));
  this.setAmountSales((((Integer) objects[8]).intValue()));
  this.setObjectives(((Integer) objects[9]).intValue());
  this.setAverageSales((((Double) objects[10]).floatValue()));
 }
}
