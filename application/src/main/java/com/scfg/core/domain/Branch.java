package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Branch extends BaseDomain {

    private String name;
    private String description;

    private Integer modalityIdc;
    private Integer status;
}
