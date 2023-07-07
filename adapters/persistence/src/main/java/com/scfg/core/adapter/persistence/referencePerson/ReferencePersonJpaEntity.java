package com.scfg.core.adapter.persistence.referencePerson;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "Pep")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ReferencePersonJpaEntity extends BaseJpaEntity {

    @Column(name = "personId")
    private String personId;

    @Column(name = "referencePersonName")
    private String referencePersonName;

    @Column(name = "referenceRelationshipIdc")
    private String referenceRelationshipIdc;

    @Column(name = "referenceTelephone")
    private String referenceTelephone;

    @Column(name = "referenceActivityIdc")
    private String referenceActivityIdc;

}
