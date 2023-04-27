package com.scfg.core.adapter.persistence.notificationUser;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class NotificationUserKey implements Serializable {
    @Column(name = "notificationId")
    public Long notificationId;

    @Column(name = "toUserId")
    public Long toUserId;
}
