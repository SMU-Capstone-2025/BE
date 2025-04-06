package com.capstone.domain.notification.repository;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.domain.notification.repository.custom.CustomNotificationRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String>, CustomNotificationRepository {
}
