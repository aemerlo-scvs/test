package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
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
public class Clause  extends BaseDomain {
    private String name;
    private String apsCode;
    private String description;
    private String resolution;
    private String documentTemplate;
    private Integer quantityParameter;
    private Integer mandatory;
    private Long productId;
    private Integer status;
}
