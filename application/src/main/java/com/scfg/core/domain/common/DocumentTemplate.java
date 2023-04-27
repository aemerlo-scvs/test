package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.Null;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DocumentTemplate extends BaseDomain{
    private String description;
    private Integer documentTypeIdc;
    private String documentUrl;
    @Null
    private long idDocumentTemplate;
}
