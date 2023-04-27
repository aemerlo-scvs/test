package com.scfg.core.adapter.persistence.policyFileDocument;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "PolicyDocument")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PolicyFileDocumentJpaEntity extends BaseJpaEntity {

    @Column(name = "policyId")
    private Long policyId;

    @Column(name = "fileDocumentId")
    private Long fileDocumentId;

    @Column(name = "isSigned")
    private Integer isSigned;

    @Column(name = "uploadDate")
    private Date uploadDate;

    @Column(name = "policyItemId")
    private Long policyItemId;

    @Column(name = "documentNumber")
    private String documentNumber;
}
