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
public class Account extends BaseDomain {

    private Integer accountTypeIdc;

    private String accountNumber;

    private Integer accountCurrencyTypeIdc;

    private Long personId;
}
