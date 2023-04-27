package com.scfg.core.adapter.persistence.notification;

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
@Table(name = "Notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = HelpersConstants.FILTER_ACTIVE_RECORDS_FOR_PERSIST)
@SuperBuilder
public class NotificationJpaEntity extends BaseJpaEntity {

    @Column(name = "fromUserId", nullable = false)
    private Long fromUserId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "redirectionUrl", nullable = false)
    private String redirectionUrl;

    @Column(name = "sentToGroup")
    private Integer sentToGroup;

    @Column(name = "roleId")
    private Long roleId;

}
