package com.scfg.core.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Classifier extends BaseDomain {

    private String abbreviation;

    @NotNull
    private String description;

    @Null
    private Integer order;

    private Long referenceId;

    @NotNull
    private Long classifierTypeId;

    private Long classifierParentId;

    private Integer enabledDelete;

    // Relationship
    private ClassifierType classifierType;
}
