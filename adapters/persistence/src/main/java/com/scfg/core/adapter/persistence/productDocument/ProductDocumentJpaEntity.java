package com.scfg.core.adapter.persistence.productDocument;

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
@Table(name = "ProductDocument")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ProductDocumentJpaEntity extends BaseJpaEntity {
    @Column(name = "description")
    private String description;
    @Column(name = "documentTypeIdc")
    private Integer documentTypeIdc;
    @Column(name = "productId")
    private Integer productId;
}
