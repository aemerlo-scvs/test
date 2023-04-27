package com.scfg.core.domain.dto;

import com.scfg.core.common.util.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
public class RankingSucursalDto extends RankingBaseDto {

    private Float averageSales;
    private Integer amountZones;

    public RankingSucursalDto(Object[] objects) {
        this.setReporte((String) objects[0]);
        //falta de la fecha
        this.setCorteMes(((Date)objects[1]));
        this.setMes((Integer) objects[2]);
        this.setYears((Integer) objects[3]);
        this.setBranchOffice((String) objects[4]);
        this.setAmountZones(((Integer) objects[5]).intValue());
        this.setAverageSales(((Double) objects[6]).floatValue());
        this.setSales(((Integer) objects[7]).intValue());
        this.setAmountSales((((Integer) objects[8]).intValue()));
        this.setObjectives(((Integer) objects[9]).intValue());

    }
}
