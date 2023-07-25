package com.scfg.core.adapter.persistence.plan;

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
@Table(name = "[Plan]")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class PlanJpaEntity extends BaseJpaEntity {
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "totalPremium")
    private Double totalPremium;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "applyDiscount")
    private Integer applyDiscount;

    @Column(name = "currencyTypeIdc")
    private Integer currencyTypeIdc;

    @Column(name = "productId")
    private Long productId;


    @Column(name = "bfsAgreementCode")
    private Integer bfsAgreementCode;


//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "productId")
//    @JsonBackReference
//    private ProductJpaEntity product;

}
