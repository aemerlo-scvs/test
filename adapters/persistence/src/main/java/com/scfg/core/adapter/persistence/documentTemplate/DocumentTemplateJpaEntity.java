package com.scfg.core.adapter.persistence.documentTemplate;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Null;

@Entity
@Table(name = "DocumentTemplate")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class DocumentTemplateJpaEntity extends BaseJpaEntity {
    @Column(name = "description")
    private String description;
    @Column(name = "documentTypeIdc")
    private Integer documentTypeIdc;
    @Column(name = "documentUrl")
    private String documentUrl;
    @Column(name = "idDocumentTemplate")
    private Long idDocumentTemplate;
}
