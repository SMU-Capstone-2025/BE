package com.capstone.domain.chat.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //TODO: JWT토큰과 연동
//    private final ChatPreHandler chatPreHandler;
//
//    public WebSocketConfig(ChatPreHandler chatPreHandler) {
//        this.chatPreHandler = chatPreHandler;
//    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        config.enableSimpleBroker("/topic","/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        registry.addEndpoint("/chat-websocket").withSockJS();
        registry.addEndpoint("/chat-websocket");
    }


//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(chatPreHandler);
//    }
}