package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
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
public class PaymentFileDocument extends BaseDomain {

    private String description;
    private Integer documentTypeIdc;
    private String documentNumber;
    private String content;
    private String mimeType;
}
