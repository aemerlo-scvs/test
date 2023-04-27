package com.scfg.core.domain.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ClassifierType extends BaseDomain {


    public ClassifierType(Long id, Date createdAt, Date lastModifiedAt, Long createdBy, Long lastModifiedBy, String description, Integer order, Long classifierTypeParentId, Long referenceId) {
        super(id, createdAt, lastModifiedAt, createdBy, lastModifiedBy);
        this.description = description;
        this.order = order;
        this.classifierTypeParentId = classifierTypeParentId;
        this.referenceId = referenceId;
        this.classifiers = new ArrayList<>();
    }

    @NotNull
    private String description;

    /*@NotNull
    private Integer state;*/

    @Null
    private Integer order;

    private Long classifierTypeParentId;

    private Long referenceId;

    // Relationship
    private List<Classifier> classifiers;


}
