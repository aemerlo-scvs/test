package com.scfg.core.adapter.persistence.notification;

import com.scfg.core.domain.NotificationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationJpaEntity, Long> {

    @Query("SELECT new com.scfg.core.domain.NotificationDTO(n.id, n.fromUserId, nu.id.toUserId, n.name, n.description, n.redirectionUrl, nu.read, nu.note, nu.createdAt, nu.lastModifiedAt)" +
            "FROM NotificationJpaEntity n " +
            "INNER JOIN NotificationUserJpaEntity nu ON nu.id.notificationId = n.id " +
            "WHERE n.status = :status AND nu.status = :status")
    List<NotificationDTO> customFindAll(@Param("status") int status);

    @Query("SELECT new com.scfg.core.domain.NotificationDTO(n.id, n.fromUserId, nu.id.toUserId, n.name, n.description, n.redirectionUrl, nu.read, nu.note, nu.createdAt, nu.lastModifiedAt)" +
            "FROM NotificationJpaEntity n " +
            "INNER JOIN NotificationUserJpaEntity nu ON nu.id.notificationId = n.id " +
            "WHERE n.status = :status AND nu.status = :status AND nu.id.toUserId = :toUserId")
    List<NotificationDTO> customFindAllToUserId(@Param("status") int status, @Param("toUserId") long toUserId);

}
