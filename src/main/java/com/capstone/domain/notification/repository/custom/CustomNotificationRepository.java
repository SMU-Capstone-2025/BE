package com.capstone.domain.notification.repository.custom;

import com.capstone.domain.notification.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomNotificationRepository {
    List<Notification> findAllByEmail(String email);
    List<Notification> findAllByExpiredDateBefore(String currentTime);
}
