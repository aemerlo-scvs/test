package com.scfg.core.adapter.persistence.notificationUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationUserRepository extends JpaRepository<NotificationUserJpaEntity, Long> {

    NotificationUserJpaEntity findById_NotificationIdAndId_ToUserId(long notificationId, long toUserId);

    @Modifying
    @Query("UPDATE NotificationUserJpaEntity " +
            "SET read = 1 " +
            "WHERE id.notificationId = :notificationId AND id.toUserId  = :toUserId")
    void updateNotificationRead(@Param("notificationId") int notificationId, @Param("toUserId") long toUserId);

}
