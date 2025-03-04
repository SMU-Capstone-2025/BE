package global.config;

import global.DocumentWebSocketHandler;
import notification.handler.NotificationWebSocketHandler;
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
    @Autowired
    private final DocumentWebSocketHandler documentWebSocketHandler;

    public WebSocketConfig(NotificationWebSocketHandler notificationWebSocketHandler, DocumentWebSocketHandler documentWebSocketHandler){
        this.notificationWebSocketHandler = notificationWebSocketHandler;
        this.documentWebSocketHandler = documentWebSocketHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationWebSocketHandler, "/ws/notifications")
                .addHandler(documentWebSocketHandler, "/ws/task")
                .setAllowedOrigins("*"); // 클라이언트 도메인 허용
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customWebServerFactory() {
        return factory -> {
            factory.addAdditionalTomcatConnectors(createWebSocketConnector());
        };
    }

    private Connector createWebSocketConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(8081); // WebSocket 포트
        return connector;
    }

}