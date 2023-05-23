package com.scfg.core.adapter.persistence.annexeFileDocument;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "AnnexeFileDocument")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnnexeFileDocumentJpaEntity extends BaseJpaEntity {
    @Column(name = "description")
    private String description;
    @Column(name = "documentTypeIdc")
    private Integer documentTypeIdc;
    @Column(name = "content")
    private String content;
    @Column(name = "mimeType")
    private String mimeType;
    @Column(name = "documentNumber")
    private Long documentNumber;
}
