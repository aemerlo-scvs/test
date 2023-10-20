package com.scfg.core.adapter.persistence.direction;

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
@Table(name = "Direction")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class DirectionJpaEntity extends BaseJpaEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "directionTypeIdc")
    private Integer directionTypeIdc;

    @Column(name = "personId")
    private Long personId;

    @Column(name= "departmentIdc")
    private Integer departmentIdc;

    @Column(name = "cityIdc")
    private Integer cityIdc;

    @Column(name = "referenceDirection")
    private String referenceDirection;

    @Column(name = "newPersonId")
    private Long newPersonId;

    @Column(name = "cellphone")
    private String cellphone;
}
