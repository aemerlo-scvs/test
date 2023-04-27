package com.scfg.core.adapter.persistence.juridicalPerson;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "JuridicalPerson")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class JuridicalPersonJpaEntity extends BaseJpaEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "businessTypeIdc")
    private Integer businessTypeIdc;

    @Column(name = "webSite")
    private String webSite;
}
