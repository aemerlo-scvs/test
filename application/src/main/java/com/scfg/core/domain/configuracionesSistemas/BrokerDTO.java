package com.scfg.core.domain.configuracionesSistemas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.exolab.castor.types.DateTime;

import javax.persistence.Column;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BrokerDTO {
    private long id;
    private String businessName;
    private long nit;
    private String address;
    private String telephone;
    private String email;
    private String city;
    private long cityIdc;
    private String approvalCode;
    private int status;
    private Date createdAt;
    private Date lastModifiedAt;
}
