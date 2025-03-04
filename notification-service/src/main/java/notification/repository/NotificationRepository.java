package notification.repository;

import notification.entity.Notification;
import notification.repository.custom.CustomNotificationRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String>, CustomNotificationRepository {
}
