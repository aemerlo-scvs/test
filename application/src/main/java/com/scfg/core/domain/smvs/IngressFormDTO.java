package com.scfg.core.domain.smvs;

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
public class IngressFormDTO {

    private String activationCode;

    private String identificationNumber;

    private String reCaptchaResponse;
}
