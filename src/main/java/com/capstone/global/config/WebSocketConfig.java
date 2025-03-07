package com.capstone.global.config;

import com.capstone.domain.notification.handler.NotificationWebSocketHandler;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private final NotificationWebSocketHandler notificationWebSocketHandler;
]
    public WebSocketConfig(NotificationWebSocketHandler notificationWebSocketHandler, DocumentWebSocketHandler documentWebSocketHandler){
        this.notificationWebSocketHandler = notificationWebSocketHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationWebSocketHandler, "/ws/notifications")
                .setAllowedOrigins("*"); // 클라이언트 도메인 허용
    }

}