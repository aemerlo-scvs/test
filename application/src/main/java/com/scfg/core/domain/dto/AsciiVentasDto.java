package com.scfg.core.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Setter
@Getter
public class AsciiVentasDto {
    private  String monthYear;
    private Date paymentDate;
    private String status;
    private Double cantActivation;
    private Long branchOfficeId;
    private String branch;
    private String zones;
    private Integer agencyId;
    private String agency;
    private Integer codCajero;
    private String namesCajero;
    private Integer cantVentas;
    private Integer mes;
    private Integer years;
    private Date courtDate;
    public AsciiVentasDto(Object[] objects){
        this.monthYear=(String)objects[0];
       this.paymentDate=(Date)objects[1];
       this.status=(String) objects[2];
       this.cantActivation=((BigDecimal)objects[3]).doubleValue();
       this.branchOfficeId=((BigInteger)objects[4]).longValue();
       this.branch=(String) objects[5];
       this.zones=(String) objects[6];
       this.agencyId=((BigInteger)objects[7]).intValue();
       this.agency=(String)objects[8];
       this.codCajero=(Integer) objects[9];
       this.namesCajero=(String) objects[10];
       this.cantVentas =(Integer) objects[11];
       this.mes=(Integer) objects[12];
       this.years=(Integer) objects[13];
       this.courtDate=(Date) objects[14];
    }
}
