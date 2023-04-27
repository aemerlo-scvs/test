package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Telephone extends BaseDomain {
    private Long personId;
    private String number;

    private Integer numberTypeIdc;

    public Telephone() {}

    public Telephone(Long personId, String number, Integer numberTypeIdc) {
        this.personId = personId;
        this.number = number;
        this.numberTypeIdc = numberTypeIdc;
    }
}
