package com.scfg.core.adapter.persistence.requestMessage;

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
@Table(name = "RequestMessage")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class RequestMessageJpaEntity extends BaseJpaEntity {

    @Column(name = "messageSentId")
    private Long messageSentId;

    @Column(name = "messageToSendId")
    private Long messageToSendId;

    @Column(name = "requestId")
    private Long requestId;

    //todo ELIMINAR
}
