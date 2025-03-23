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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final NotificationWebSocketHandler notificationWebSocketHandler;
    private final SimpMessageSendingOperations messagingTemplate;



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
            JsonNode rootNode = objectMapper.readTree(message);

            // "log" 필드 안의 문자열을 다시 파싱 (이건 JSON 문자열임)
            String logString = rootNode.get("log").asText();
            JsonNode logNode = objectMapper.readTree(logString);
            log.info("logNode : {}", logNode);

            // editors 필드 가져오기
            JsonNode editorsNode = logNode.get("editors");
            List<String> owners = new ArrayList<>();
            if (editorsNode != null && editorsNode.isArray()) {
                for (JsonNode editor : editorsNode) {
                    owners.add(editor.asText());
                }
            }

            // LogEntity 매핑 (원한다면 logNode로 매핑)
            LogEntity logEntity = objectMapper.treeToValue(logNode, LogEntity.class);

            String notificationContent = MessageGenerator.generateFromDto(MessageGenerator.TASK_CREATED, logNode);
            Notification notification = createNotification(notificationContent, owners);

            saveNotification(notification);

            sendNotificationByOwnersId(owners, notificationContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotificationByOwnersId(List<String> editors, String content){
        editors.forEach(ownerEmail -> {
            messagingTemplate.convertAndSend("/sub/notification/" + ownerEmail, content);
        });
    }

    public void processUpdateNotification(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});

        // 필수 데이터 추출
        String projectName = (String) map.getOrDefault("targetId", "Unknown Project");
        String descrption = (String) map.getOrDefault("targetId", "변동사항 없음.");
        String method = (String) map.get("method");

        if (method == null) {
            return;
        }

        // 공통 데이터 저장
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("projectName", projectName);

        // 템플릿 선택
        String template;
        List<String> emails = new ArrayList<>();
        List<String> authorities = new ArrayList<>();

        // "data" 필드 처리 (필요한 경우만 사용)
        Map<String, String> dataMap = (Map<String, String>) map.get("data");
        if (dataMap != null) {
            emails = new ArrayList<>(dataMap.keySet());
            authorities = new ArrayList<>(dataMap.values());
        }

        switch (method) {
            case "UPDATE":
                messageData.put("description", descrption);
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

        // 메시지 생성
        String notificationContent = MessageGenerator.generateFromDto(template, messageData);

        // 알림 저장 및 전송
        Notification notification = createNotification(notificationContent, emails);
        saveNotification(notification);
        notificationWebSocketHandler.broadcastMessage(notificationContent);
    }


    public Notification findNotificationByIdOrThrow(String id){
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(NotificationMessages.NOTIFICATION_NOT_FOUND));
    }
}

