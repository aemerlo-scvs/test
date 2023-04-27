package com.scfg.core.domain;

import com.scfg.core.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class FileDocument extends BaseDomain {
    private String description;
    private Integer typeDocument;
    private String directoryLocation;
    private String content;
    private String mime;
    private Integer cite;
}
