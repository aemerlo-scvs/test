package com.scfg.core.adapter.persistence.sequencePolicy;

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
import java.time.LocalDate;

@Entity
@Table(name = "SequencePolicy")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class SequencePolicyJpaEntity extends BaseJpaEntity {

    @Column(name = "policyId")
    private Long policyId;

    @Column(name = "requestNumber")
    private Long requestNumber;

    @Column(name = "certificateCoverageNumber")
    private Long certificateCoverageNumber;
}
