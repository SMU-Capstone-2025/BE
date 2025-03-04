package notification.scheduler;

import notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 오전 4시 53분 30초에 실행
    public void scheduleDeleteExpiredNotifications() {
        notificationService.deleteExpiredNotification();
    }
}