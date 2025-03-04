package notification.repository.custom;

import notification.entity.Notification;

import java.util.List;

public interface CustomNotificationRepository {
    List<Notification> findAllByEmail(String email);
    List<Notification> findAllByExpiredDateBefore(String currentTime);
}
