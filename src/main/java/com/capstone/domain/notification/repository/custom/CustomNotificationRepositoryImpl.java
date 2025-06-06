package com.capstone.domain.notification.repository.custom;

import com.capstone.domain.notification.entity.Notification;
import com.capstone.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository{
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Notification> findAllByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Notification> notifications = mongoTemplate.find(query, Notification.class);

        // 시간 형식 변환
        notifications.forEach(notification -> {
            if (notification.getExpiredDate() != null) {
                notification.setExpiredDate(DateUtil.formatLocalDateTime(notification.getExpiredDate()));
            }
        });

        return notifications;
    }
    @Override
    public List<Notification> findAllByExpiredDateBefore(String currentTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("expiredDate").lt(currentTime)); // $lt 조건 추가

        return mongoTemplate.find(query, Notification.class);
    }
}
