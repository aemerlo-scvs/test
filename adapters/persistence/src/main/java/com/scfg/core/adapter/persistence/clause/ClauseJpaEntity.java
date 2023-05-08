package com.scfg.core.adapter.persistence.clause;

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
@Table(name = "Clause")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ClauseJpaEntity extends BaseJpaEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "apsCode")
    private String apsCode;
    @Column(name = "description")
    private String description;
    @Column(name = "resolution")
    private String resolution;
    @Column(name = "documentTemplate")
    private String documentTemplate;
    @Column(name = "quantityParameter")
    private Integer quantityParameter;
    @Column(name = "mandatory")
    private Integer mandatory;
    @Column(name = "productId")
    private Long productId;
    //[name] [varchar](250) NULL,
//            [apsCode] [varchar](100) NULL,
//            [description] [varchar](250) NULL,
//            [resolution] [varchar](250) NULL,
//            [documentTamplate] [varchar](max) NULL,
//            [quantityParameter] [int] NULL,
//            [mandatory] [int] NULL,
//            [productId] [bigint] NULL,
}
