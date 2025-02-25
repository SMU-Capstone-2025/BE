package com.capstone.domain.chat.config;


//import com.capstone.domain.chat.interceptor.WebSocketSecurityInterceptor;
//import com.capstone.domain.chat.interceptor.WebSocketSecurityInterceptor;
import com.capstone.domain.chat.interceptor.WebSocketSecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketSecurityInterceptor interceptors;

    public WebSocketConfig(WebSocketSecurityInterceptor interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        config.enableSimpleBroker("/topic","/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        registry.addEndpoint("/chat-websocket").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/chat-websocket").setAllowedOriginPatterns("*");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(interceptors);
    }
}