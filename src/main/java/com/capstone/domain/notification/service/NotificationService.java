package com.capstone.domain.notification.service;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.exception.NotificationNotFoundException;
import com.capstone.domain.notification.handler.NotificationHandler;
import com.capstone.domain.notification.message.NotificationMessages;
import com.capstone.domain.notification.repository.NotificationRepository;

import com.capstone.global.jwt.JwtUtil;
import com.capstone.global.kafka.dto.CommonChangePayload;
import com.capstone.global.kafka.message.MessageGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.capstone.domain.notification.entity.Notification.createNotification;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final List<NotificationHandler> handlers;
    private final SimpMessageSendingOperations messagingTemplate;
    private final JwtUtil jwtUtil;


    public List<Notification> findAllNotifications(String token){
        return notificationRepository.findAllByEmail(jwtUtil.getEmail(token));
    }

    @Transactional
    public String saveNotification(Notification notification){
        notificationRepository.save(notification);
        return NotificationMessages.NOTIFICATION_SAVED;
    }

    @Transactional
    public String markNotificationAsRead(String id){
        Notification notification = findNotificationByIdOrThrow(id);
        notification.setRead(true); // read 유무에 따라 프론트단에서 하이라이트를 다르게 하던지 할 필요가 있어보임.
        saveNotification(notification);

        return notification.getId(); // 프론트 단에서 변경된 알림의 아이디 값을 알면 하이라이트 변경을 알 수 있지 않을까?
    }

    @Transactional
    public void deleteExpiredNotification(){
        List<Notification> notifications = notificationRepository.findAllByExpiredDateBefore(LocalDateTime.now().toString());
        notificationRepository.deleteAll(notifications);
    }

    public void sendNotificationByOwnersId(List<String> editors, String content){
        editors.forEach(ownerEmail -> {
            messagingTemplate.convertAndSend("/sub/notification/" + ownerEmail, content);
        });
    }

    @Transactional
    public <T extends CommonChangePayload> void processUpdateNotification(String kafkaTopic, T payload) {

        try {
            Optional<NotificationHandler> matchedHandler = findProperHandler(handlers, kafkaTopic);

            matchedHandler.ifPresent(handler -> {
                String notificationContent = handler.generateMessage(payload);


                Notification notification = createNotification(notificationContent, payload.getCoworkers());
                saveNotification(notification);
                sendNotificationByOwnersId(payload.getCoworkers(), notificationContent);
            });

            // 핸들러 못 찾은 경우 로그 남기기
            if (matchedHandler.isEmpty()) {
                log.warn("No matching handler found for method: {}", kafkaTopic);
            }
        } catch (Exception e) {
            log.error("Failed to parse notification message: {}", payload, e);
            throw new RuntimeException("Invalid notification message format", e);
        }
    }

    public Optional<NotificationHandler> findProperHandler(List<NotificationHandler> handlers, String kafkaTopic){
        return handlers.stream()
                .filter(handler -> handler.canHandle(kafkaTopic))
                .findFirst();
    }


    public Notification findNotificationByIdOrThrow(String id){
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(NotificationMessages.NOTIFICATION_NOT_FOUND));
    }
}

