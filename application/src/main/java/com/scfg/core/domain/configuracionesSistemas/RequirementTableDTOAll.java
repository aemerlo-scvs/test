package com.scfg.core.domain.configuracionesSistemas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;

// @AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class RequirementTableDTOAll {
    private Long id;
    private Date createdAt;

    private Integer startAge;
    private Integer finishAge;

    private Double initialAmount;
    private Double finalAmount;

    private String requirement;

    private Long productId;
    private String product;

    private Integer status;

    public RequirementTableDTOAll(Long id, Date createdAt, Integer startAge, Integer finishAge, Double initialAmount, Double finalAmount, String requirement, Long productId, String product, Integer status) {
        this.id = id;
        this.createdAt = createdAt;
        this.startAge = startAge;
        this.finishAge = finishAge;
        this.initialAmount = initialAmount;
        this.finalAmount = finalAmount;
        this.requirement = requirement;
        this.productId = productId;
        this.product = product;
        this.status = status;
    }
}
