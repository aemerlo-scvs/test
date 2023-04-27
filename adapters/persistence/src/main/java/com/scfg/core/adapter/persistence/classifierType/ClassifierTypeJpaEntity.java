package com.scfg.core.adapter.persistence.classifierType;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = HelpersConstants.TABLE_CLASSIFIER_TYPE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ClassifierTypeJpaEntity extends BaseJpaEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "[order]")
    private Integer order;

    @Column(name = "classifierTypeParentId")
    private Long classifierTypeParentId;

    @Column(name = "referenceId", nullable = false)
    private Long referenceId;

    // Relationship
//     @OneToMany(mappedBy = "classifierType",fetch = FetchType.LAZY)
//     @JsonManagedReference
//    private List<ClassifierJpaEntity> classifiers;

    @OneToMany(mappedBy = "classifierType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@Fetch(FetchMode.JOIN)
    @JsonManagedReference
    private List<ClassifierJpaEntity> classifiers;

}


