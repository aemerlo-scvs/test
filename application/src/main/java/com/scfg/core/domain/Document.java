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

public class Document extends BaseDomain {
    private long personId;
    private String description;
    private String content;
    private String mimeType;
    private Integer documentTypeIdc;
    private String documentUrl;
    private Long documentNumber;
}
