package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Broker extends BaseDomain {

    private String businessName;
    private long nit;
    private String address;
    private String telephone;
    private String email;
    private long cityIdc;
    private String approvalCode;
    private Integer status;
}
