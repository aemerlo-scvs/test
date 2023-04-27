package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OperationItem extends BaseDomain {

    private Integer operationTerm;

    private Double currentAmount;

    private Integer monthIdc;

    private Integer yearIdc;

    private Long operationHeaderId;

}
