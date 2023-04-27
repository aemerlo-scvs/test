package com.scfg.core.adapter.persistence.document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.adapter.persistence.person.PersonJpaEntity;
import com.scfg.core.adapter.persistence.policy.PolicyJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "Document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class DocumentJpaEntity extends BaseJpaEntity {



    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "documentTypeIdc")
    private Integer documentTypeIdc;

    @Column(name = "documentUrl")
    private String documentUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personId")
    @JsonBackReference
    private PersonJpaEntity personJpaEntity;

    @Column(name = "content")
    private String content;

    @Column(name = "mimeType")
    private String mimeType;
}
