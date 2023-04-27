package com.scfg.core.adapter.persistence.notificationUser;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.scfg.core.adapter.persistence.ChangeLogListener;
import com.scfg.core.adapter.persistence.notification.NotificationJpaEntity;
import com.scfg.core.adapter.persistence.user.UserJpaEntity;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

import static com.scfg.core.common.util.HelpersMethods.isNull;

@Entity
@Table(name = "NotificationUser")
@Data
@EntityListeners({AuditingEntityListener.class})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NotificationUserJpaEntity {

    @EmbeddedId
    NotificationUserKey id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "notificationId", insertable = false, updatable = false)
    @JsonBackReference
    private NotificationJpaEntity notification;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "toUserId", insertable = false, updatable = false)
    @JsonBackReference
    private UserJpaEntity user;

    @Column(name = "[read]")
    private Integer read;

    @Column(name = "note")
    private String note;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "lastModifiedAt")
    private Date lastModifiedAt;

    // Outgoing relationship
    @PrePersist
    public void prePersistNotificationUser(){
        status = PersistenceStatusEnum
                .CREATED_OR_UPDATED
                .getValue();
        read = PersistenceStatusEnum.DELETED.getValue();
    }

    @PreUpdate
    public void preUpdateNotificationUser(){
        if (isNull(status)) {
            status = PersistenceStatusEnum
                    .CREATED_OR_UPDATED
                    .getValue();
        }
    }

}
