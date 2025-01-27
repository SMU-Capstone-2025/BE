package com.capstone.domain.notification.repository;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.repository.custom.CustomNotificationRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String>, CustomNotificationRepository {
}
