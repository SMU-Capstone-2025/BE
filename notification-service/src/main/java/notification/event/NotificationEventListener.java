package notification.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notification.entity.LogEntity;
import notification.entity.Notification;
import notification.handler.NotificationWebSocketHandler;
import notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketHandler notificationWebSocketHandler;
    private final ObjectMapper objectMapper;

    public NotificationEventListener(NotificationRepository notificationRepository,
                                     NotificationWebSocketHandler notificationWebSocketHandler,
                                     ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationWebSocketHandler = notificationWebSocketHandler;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "notification-events", groupId = "notification-service-group")
    public void processNotification(String message) {
        try {
            LogEntity log = objectMapper.readValue(message, LogEntity.class);

            // 🔥 새로운 알림 생성
            Notification notification = Notification.builder()
                    .email(log.getEmail())
                    .content("📢 새로운 로그 기록: " + log.getLog())
                    .expiredDate(LocalDateTime.now().plusDays(7).toString()) // 알림 만료기간 7일
                    .isRead(false)
                    .build();

            // 🔥 알림을 데이터베이스에 저장
            notificationRepository.save(notification);

            // 🔥 WebSocket을 통해 실시간 알림 전송
            notificationWebSocketHandler.broadcastMessage(notification);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
}