package com.scfg.core.adapter.persistence.coverageProduct;

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
@Table(name = "CoverageProduct")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class CoverageProductJpaEntity extends BaseJpaEntity {

    // Cambiar esto en una relación influye en la consulta de ProductRepository
    @Column(name = "productId")
    private Long productId;

    // Cambiar esto en una relación influye en la consulta de ProductRepository
    @Column(name = "coverageId")
    private Long coverageId;

}
