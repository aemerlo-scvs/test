package com.scfg.core.domain.smvs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class VerifyActivationCodeDTO {

    public VerifyActivationCodeDTO(Long requestId, Long personId, Integer requestStatusIdc) {
        this.requestId = requestId;
        this.personId = personId;
        this.requestStatusIdc = requestStatusIdc;
    }

    private Long requestId;

    private Long personId;

    private Integer requestStatusIdc;

}
