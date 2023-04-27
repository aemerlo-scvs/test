package com.scfg.core.domain.liquidationMortgageRelief;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class MortgageReliefItem extends BaseDomain {

    @NotNull
    private long monthIdc;

    @NotNull
    private long yearIdc;

    @NotNull
    private long reportTypeIdc;

    @NotNull
    private long policyTypeIdc;

    @NotNull
    private long usersId;

    @NotNull
    private long insurancePolicyHolderIdc;

    // For register
    /*@NotNull
    private*/





}
