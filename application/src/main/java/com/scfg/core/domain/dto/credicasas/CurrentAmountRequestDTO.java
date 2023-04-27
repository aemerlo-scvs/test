package com.scfg.core.domain.dto.credicasas;

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
public class CurrentAmountRequestDTO {
    private Long id;
    private Long planId;
    private Integer requestStatusIdc;
    private Double currentAmount;

    private String exclusionComment;

    public CurrentAmountRequestDTO(Object[] columns) {
        this.id = (columns[0] != null) ? Long.parseLong(columns[0].toString()) : 0;
        this.planId = (columns[1] != null) ? (Long) Long.parseLong(columns[1].toString()) : 0;
        this.requestStatusIdc = (columns[2] != null) ? (Integer) columns[2] : 0;
        this.currentAmount = (columns[3] != null) ? (Double) columns[3] : 0;
        this.exclusionComment = (columns[4] != null) ? (String) columns[4] : "";
    }
}
