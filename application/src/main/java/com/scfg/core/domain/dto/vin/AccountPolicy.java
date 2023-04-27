package com.scfg.core.domain.dto.vin;

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
public class AccountPolicy extends BaseDomain {

    private Long accountId;

    private Long policyId;

    private Integer typeIdc;
}
