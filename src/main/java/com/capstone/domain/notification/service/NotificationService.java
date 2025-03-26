package com.capstone.domain.notification.service;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.exception.NotificationNotFoundException;
import com.capstone.domain.notification.handler.NotificationWebSocketHandler;
import com.capstone.domain.notification.message.NotificationMessages;
import com.capstone.domain.notification.repository.NotificationRepository;

import com.capstone.global.jwt.JwtUtil;
import com.capstone.global.kafka.message.MessageGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.capstone.domain.notification.entity.Notification.createNotification;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final NotificationWebSocketHandler notificationWebSocketHandler;
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

    @Transactional
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


            String notificationContent = MessageGenerator.generateFromDto(MessageGenerator.TASK_CREATED, logNode);
            Notification notification = createNotification(notificationContent, owners);

            saveNotification(notification);

            sendNotificationByOwnersId(owners, notificationContent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotificationByOwnersId(List<String> editors, String content){
        log.info("content: {}", content);
        editors.forEach(ownerEmail -> {
            messagingTemplate.convertAndSend("/sub/notification/" + ownerEmail, content);
        });
    }

    @Transactional
    public void processUpdateNotification(String message) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(message);
        log.info("message: {}", rootNode);

        String email = rootNode.get("email").asText();
        String method = rootNode.get("method").asText();
        String topic = rootNode.get("topic").asText();
        JsonNode data = rootNode.get("data");

        if (method == null) {
            return;
        }


        // 템플릿 선택
        String template;
        List<String> emails = new ArrayList<>();
        List<String> authorities = new ArrayList<>();
        String notificationContent = " ";

        log.info("method : {}", method);
        switch (method) {
            case "UPDATE":
                if (topic.equals("TASK")) {
                    emails.add(email);
                    template = MessageGenerator.TASK_UPDATED;

                    // 필요한 값만 추출
                    Map<String, Object> merged = new HashMap<>();
                    merged.put("email", email); // 루트의 이메일
                    merged.put("title", data.get("title").asText()); // data 안의 title

                    notificationContent = MessageGenerator.generateFromDto(template, merged);
                } else {
                    template = MessageGenerator.PROJECT_UPDATED;
                    notificationContent = MessageGenerator.generateFromDto(template, data); // 예: 프로젝트 업데이트 내용
                }

                log.info("NotificationContent: {}", notificationContent);
                break;
            /*case "AUTH":
                messageData.put("names", emails);
                messageData.put("authorities", authorities);
                template = MessageGenerator.AUTH_UPDATED;
                break;
            */
            case "REGISTER":
                template = MessageGenerator.PROJECT_CREATED;
                break;

            case "INVITE":
                template = MessageGenerator.PROJECT_INVITED;
                break;

            default:
                return;
        }


        // 알림 저장 및 전송
        Notification notification = createNotification(notificationContent, emails);
        saveNotification(notification);
        sendNotificationByOwnersId(emails, notificationContent);
    }


    public Notification findNotificationByIdOrThrow(String id){
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(NotificationMessages.NOTIFICATION_NOT_FOUND));
    }
}

