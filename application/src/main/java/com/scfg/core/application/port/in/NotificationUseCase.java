package com.scfg.core.application.port.in;

import com.scfg.core.domain.NotificationDTO;
import com.scfg.core.domain._NotificationDTO;
import com.scfg.core.domain.common.Menu;

import java.util.List;

public interface NotificationUseCase {
    List<NotificationDTO> getAll();

    List<NotificationDTO> getAllToUserId(Long userId);

    Boolean updateToRead(NotificationDTO notificationDTO);

    Boolean updateNote(NotificationDTO notificationDTO);

    Boolean save(_NotificationDTO notificationDTO);
}
