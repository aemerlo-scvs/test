package com.scfg.core.adapter.persistence.fileDocument;

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

@Entity
@Table(name = "FileDocument")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class FileDocumentJpaEntity extends BaseJpaEntity {
    @Column(name = "description")
    private String description;
    @Column(name = "typeDocument")
    private Integer typeDocument;
    @Column(name = "directoryLocation")
    private String directoryLocation;
    @Column(name = "content")
    private String content;
    @Column(name = "mimeType")
    private String mimeType;
    @Column(name = "cite")
    private Integer cite;

}
