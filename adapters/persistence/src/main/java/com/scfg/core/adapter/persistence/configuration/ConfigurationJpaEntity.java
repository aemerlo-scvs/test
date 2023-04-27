package com.scfg.core.adapter.persistence.configuration;

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
@Table(name = "Configuration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class ConfigurationJpaEntity extends BaseJpaEntity {

    @Column(name = "numberFormat", length = 20)
    private String numberFormat;

    @Column(name = "numberDigits")
    private Integer numberDigits;

    @Column(name = "dateFormat", length = 100)
    private String dateFormat;

    @Column(name = "timeFormat", length = 100)
    private String timeFormat;

}
