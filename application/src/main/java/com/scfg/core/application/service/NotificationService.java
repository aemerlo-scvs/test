package com.scfg.core.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.scfg.core.application.port.in.NotificationUseCase;
import com.scfg.core.application.port.out.NotificationPort;
import com.scfg.core.common.websocket.notification.handler.NotificationWebSocketHandler;
import com.scfg.core.domain.NotificationDTO;
import com.scfg.core.domain._NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {

    private final NotificationPort notificationPort;

    private final List<WebSocketSession> webSocketSessions = NotificationWebSocketHandler.getWebSocketSessions();

    public void sendWebToSocket(List<NotificationDTO> notificationDTOList) {
        for (WebSocketSession session : webSocketSessions) {
            String aux = session.getUri().toString();
            int posI = aux.lastIndexOf("/");
            String toUserId = aux.substring(posI + 1);
            notificationDTOList.forEach(item -> {
                if (item.getToUserId() == Long.parseLong(toUserId)) {
                    try {
                        String json = new ObjectMapper().writeValueAsString(item);
                        session.sendMessage(new TextMessage(json));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public List<NotificationDTO> getAll() {
        return notificationPort.findAll();
    }

    @Override
    public List<NotificationDTO> getAllToUserId(Long userId) {
        return notificationPort.findByToUserId(userId);
    }

    @Transactional
    @Override
    public Boolean updateToRead(NotificationDTO notificationDTO) {
        return notificationPort.updateToRead(notificationDTO);
    }

    @Transactional
    @Override
    public Boolean updateNote(NotificationDTO notificationDTO) {
        return notificationPort.updateNote(notificationDTO);
    }

    @Transactional
    @Override
    public Boolean save(_NotificationDTO notificationDTO) {
        List<NotificationDTO> notificationDTOList = notificationPort.save(notificationDTO);
        sendWebToSocket(notificationDTOList);
        return true;
    }
}
