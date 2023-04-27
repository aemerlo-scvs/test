package com.scfg.core.domain.dto;

import com.scfg.core.common.util.DateUtils;
import com.scfg.core.common.util.HelpersConstants;
import com.scfg.core.common.util.HelpersMethods;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.exception.DataException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RankingCajeros {
    private String func;
    private String cargo;
    private Date corteMes;
    private Integer mes;
    private Integer years;
    private Integer sellerId;
    private String sellerName;
    private Integer agencyId;
    private String agencyName;
    private Integer branchOfficeId;
    private String branchOffice;
    private String zones;
    private String requestStatus;
    private Integer ventas;
    private Date fechaIng;
    private Double antiguiedad;
    private Integer cantCajeros;
    private  String tiene;
    private String rango;

    public RankingCajeros(Object[] objects){
        this.func=(String)objects[0];
        this.cargo=(String) objects[1];
        this.corteMes=(Date) objects[2];
        this.mes=((Integer)objects[3]).intValue();
        this.years=((Integer)objects[4]). intValue();
        this.sellerId=(Integer) objects[5];
        this.sellerName=(String) objects[6];
        this.agencyId=((BigInteger)objects[7]).intValue();
        this.agencyName=(String)objects[8];
        this.branchOfficeId=((BigInteger) objects[9]).intValue();
        this.branchOffice=(String) objects[10];
        this.zones =(String) objects[11];
        this.requestStatus=(String) objects[12];
        this.ventas=(Integer) objects[13];
        this.fechaIng=DateUtils.asDate( HelpersMethods.formatStringToDate((String) objects[14]));
        this.cantCajeros=(Integer) objects[16];
        this.antiguiedad=((BigDecimal) objects[17]).doubleValue();
        this.tiene=(String) objects[18];
        this.rango=(String) objects[19];
    }
}
