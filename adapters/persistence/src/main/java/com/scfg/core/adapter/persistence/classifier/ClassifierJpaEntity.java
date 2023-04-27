package com.scfg.core.adapter.persistence.classifier;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.classifierType.ClassifierTypeJpaEntity;
import com.scfg.core.adapter.persistence.request.InsuranceRequestJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;
//import org.hibernate.annotations.CascadeType;


import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;
import java.util.Objects;

import static com.scfg.core.common.util.HelpersMethods.isNull;

@Entity
@Table(name = HelpersConstants.TABLE_CLASSIFIER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ClassifierJpaEntity extends BaseJpaEntity {

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "[order]")
    private Integer order;

    /*@Column(name = "classifierTypeId")
    private Long classifierTypeId;*/

    /*@ManyToOne
    @JoinColumn(name = "classifierParentId", referencedColumnName = "id")
    @JsonBackReference
    private ClassifierJpaEntity classifierParent;*/


    // modifier relationship recursive
    @Column(name = "classifierParentId")
    private Long classifierParentId;


    // code for enumerations
    @Column(name = "referenceId", nullable = false)
    private Long referenceId;

    //


    @Column(name = "enabledDelete", nullable = false)
    private Integer enabledDelete;

    // Relationships foreign keys
    // Outgoing relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classifierTypeId", nullable = false, updatable = false)
    @JsonBackReference
    @NotFound(action = NotFoundAction.IGNORE)
    private ClassifierTypeJpaEntity classifierType;

    // incoming relationship
   /* @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "requestStatus")
    @JsonManagedReference
    private List<InsuranceRequestJpaEntity> insuranceRequests;*/


    @PrePersist
    public void prePersistClassifier() {
        enabledDelete = 1;
    }

    @PreUpdate
    public void preUpdateClassifier() {
        if (isNull(enabledDelete)) {
            enabledDelete = 1;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClassifierJpaEntity that = (ClassifierJpaEntity) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }
}
