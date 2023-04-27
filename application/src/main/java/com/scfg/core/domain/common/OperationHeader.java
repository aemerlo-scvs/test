package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OperationHeader extends BaseDomain {

    private String operationNumber;

    private Integer operationTypeIdc;

    private Float disbursedAmount;

    private LocalDateTime disbursedDate;

    private Long policyItemId;

    private Long parentId;
}
