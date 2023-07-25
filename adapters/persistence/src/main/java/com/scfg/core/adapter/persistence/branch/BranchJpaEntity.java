package com.scfg.core.adapter.persistence.branch;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Branch")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class BranchJpaEntity extends BaseJpaEntity {

    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "description", length = 150)
    private String description;
    @Column(name = "modalityIdc")
    private Integer modalityIdc;

}
