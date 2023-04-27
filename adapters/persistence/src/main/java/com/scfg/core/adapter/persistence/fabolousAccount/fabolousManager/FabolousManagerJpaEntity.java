package com.scfg.core.adapter.persistence.fabolousAccount.fabolousManager;

import com.scfg.core.adapter.persistence.BaseJpaEntity;
import com.scfg.core.common.util.HelpersConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = HelpersConstants.TABLE_FBS_MANAGER)
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class FabolousManagerJpaEntity extends BaseJpaEntity {

    private String managerName;

    private Long managerCode;

}
