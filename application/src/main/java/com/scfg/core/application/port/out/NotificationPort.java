package com.scfg.core.application.port.out;

import com.scfg.core.domain.NotificationDTO;
import com.scfg.core.domain._NotificationDTO;

import java.util.List;

public interface NotificationPort {
    List<NotificationDTO> findAll();

    List<NotificationDTO> findByToUserId(long toUserId);

    Boolean updateToRead(NotificationDTO notificationDTO);

    Boolean updateNote(NotificationDTO notificationDTO);

    List<NotificationDTO> save(_NotificationDTO notificationDTO);
}
