package com.scfg.core.adapter.persistence.coverage;

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
@Table(name = "Coverage")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class CoverageJpaEntity extends BaseJpaEntity {
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "code")
    private Integer code;

    @Column(name = "coverageTypeIdc")
    private Integer coverageTypeIdc;

    @Column(name = "apsCode")
    private String apsCode;

    @Column(name = "resolution")
    private String resolution;

    @Column(name = "minimumAge")
    private Integer minimumAge;
    @Column(name = "maximumAge")
    private Integer maximumAge;
    @Column(name = "limitAge")
    private  Integer limitAge;
    @Column(name = "productId")
    private Long productId;
}
