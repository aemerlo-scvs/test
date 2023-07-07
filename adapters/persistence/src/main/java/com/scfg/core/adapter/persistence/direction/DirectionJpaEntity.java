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
    @Column(name = "internalNumber")
    private String internalNumber;
    @Column(name = "telephoneTypeIdc")
    private Integer telephoneTypeIdc;
    @Column(name = "directionTypeIdc")
    private Integer directionTypeIdc;
    //todo esta columna no corresponde, debe eliminarse
    @Column(name = "cellPhone")
    private String cellPhone;
    @Column(name = "personId")
    private Long personId;
    @Column(name = "newPersonId")
    private Long newPersonId;
}
