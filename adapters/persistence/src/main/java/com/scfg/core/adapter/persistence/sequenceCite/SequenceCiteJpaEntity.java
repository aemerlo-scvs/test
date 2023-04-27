package com.scfg.core.adapter.persistence.sequenceCite;

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
@Table(name = "SequenceCite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class SequenceCiteJpaEntity extends BaseJpaEntity {
    @Column(name = "citeNumber")
    private Long citeNumber;

    @Column(name = "companyIdc")
    private Long companyIdc;

    @Column(name = "year")
    private String year;
}
