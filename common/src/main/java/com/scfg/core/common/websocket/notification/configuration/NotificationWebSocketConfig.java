package com.scfg.core.common.websocket.notification.configuration;

import com.scfg.core.common.websocket.notification.handler.NotificationWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class NotificationWebSocketConfig implements WebSocketConfigurer {

    private static final String NOTIFICATION_ENDPOINT = "/notificationWebSocket/{userId}";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(getWebSocketHandler(), NOTIFICATION_ENDPOINT)
                .setAllowedOrigins("*");
    }

    @Bean
    public static WebSocketHandler getWebSocketHandler() {
        return new NotificationWebSocketHandler();
    }

}
