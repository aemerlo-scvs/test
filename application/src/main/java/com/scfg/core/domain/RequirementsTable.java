package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RequirementsTable extends BaseDomain {
    private Double startAge;
    private Double finishAge;
    private Double initialAmount;
    private Double finalAmount;
    private String description;
    private Long planId;
}
