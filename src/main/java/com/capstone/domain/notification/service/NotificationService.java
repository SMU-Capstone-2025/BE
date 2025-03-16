package com.capstone.domain.notification.service;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.exception.NotificationNotFoundException;
import com.capstone.domain.notification.handler.NotificationWebSocketHandler;
import com.capstone.domain.notification.message.NotificationMessages;
import com.capstone.domain.notification.repository.NotificationRepository;
import com.capstone.global.elastic.entity.LogEntity;
import com.capstone.global.kafka.message.MessageGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.capstone.domain.notification.entity.Notification.createNotification;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final NotificationWebSocketHandler notificationWebSocketHandler;



    public List<Notification> findAllNotifications(String email){
        return notificationRepository.findAllByEmail(email);
    }

    public String saveNotification(Notification notification){
        notificationRepository.save(notification);
        return NotificationMessages.NOTIFICATION_SAVED;
    }

    public String markNotificationAsRead(String id){
        Notification notification = findNotificationByIdOrThrow(id);
        notification.setRead(true); // read 유무에 따라 프론트단에서 하이라이트를 다르게 하던지 할 필요가 있어보임.
        saveNotification(notification);

        return notification.getId(); // 프론트 단에서 변경된 알림의 아이디 값을 알면 하이라이트 변경을 알 수 있지 않을까?
    }

    public void deleteExpiredNotification(){
        List<Notification> notifications = notificationRepository.findAllByExpiredDateBefore(LocalDateTime.now().toString());
        notificationRepository.deleteAll(notifications);

    }

    public void processLogNotification(String message) {
        try {
            LogEntity log = objectMapper.readValue(message, LogEntity.class);
            String notificationContent = MessageGenerator.generateFromDto(MessageGenerator.TASK_CREATED, log);
            Notification notification = createNotification(notificationContent);
            saveNotification(notification);
            notificationWebSocketHandler.broadcastMessage(notificationContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public void processUpdateNotification(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});

        Map<String, Object> messageMap = (Map<String, Object>) map.get("data");
        String projectName = messageMap.get("target").toString();

        String method = (String) map.get("method");
        if (method == null) {
            return;
        }

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("projectName", projectName);

        String template;

        List<String> emails = (List<String>) messageMap.get("emails");
        List<String> authorities;

        Map<String, String> authoritiesMap = (Map<String, String>) messageMap.get("authorities");
        authorities = new ArrayList<>(authoritiesMap.values());


        switch (method) {
            case "UPDATE":
                messageData.put("description", "변동사항 없음.");
                template = MessageGenerator.PROJECT_UPDATED;
                break;
            case "AUTH":
                messageData.put("names", emails);
                messageData.put("authorities", authorities);
                template = MessageGenerator.AUTH_UPDATED;
                break;
            case "REGISTER":
                template = MessageGenerator.PROJECT_CREATED;
                break;
            case "INVITE":
                template = MessageGenerator.PROJECT_INVITED;
                break;
            default:
                return;
        }

        String notificationContent = MessageGenerator.generateFromDto(template, messageData);
        Notification notification = createNotification(notificationContent, emails);
        saveNotification(notification);
        notificationWebSocketHandler.broadcastMessage(notificationContent);
    }


    public Notification findNotificationByIdOrThrow(String id){
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(NotificationMessages.NOTIFICATION_NOT_FOUND));
    }
}

